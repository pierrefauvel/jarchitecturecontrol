package org.jarco.code.internal;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jarco.code.external.EModifier;
import org.jarco.code.external.IAnnotation;
import org.jarco.code.external.IClass;
import org.jarco.code.external.ICodeRepository;
import org.jarco.code.external.IField;
import org.jarco.code.external.IMethod;
import org.jarco.code.external.IPackage;
import org.jarco.code.external.IProject;
import org.jarco.code.external.IType;
import org.jarco.code.internal.bcel.BCELUtilities;
import org.jarco.collections.ImmutableList;
import org.jarco.collections.ImmutableNamedList;
import org.jarco.collections.ImmutableNamedMap;
import org.jarco.collections.ImmutableSet;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;

//TODO V1.1 EXTERNALISER LE BCEL de ClassInternal

//TODO V1.1 Cacher (soft reference + dynamic proxy) de manière très générale toutes les méthodes de consultation des ICodeElement
public class ClassInternal extends ACodeElementInternal implements IClass {

	private IProject project;
	private IPackage pkg;
	private String longName;
	private String shortName;
	
	private Class reflectionPeer;
	private SoftReference<JavaClass> bcelPeer;
	private String zip_file;
	private String file_name;
	private String parentClassName;
	private IClass parentClass;
	
	public ClassInternal(ICodeRepository repo, IProject mavenProjectProxy, IPackage pkg,String ln, String sn, String zip_file,String file_name, String parent) {
		super(repo);
		if(ln==null)
			throw new RuntimeException("ClassImpl receives null class name");
		this.project=mavenProjectProxy;
		this.pkg = pkg;
		this.longName=ln;
		this.shortName=sn;
		this.zip_file=zip_file;
		this.file_name=file_name;
		this.parentClassName=parent;
		((CodeRepositoryInternal)(repo)).registerInnerClass(parent,this);
	}
	
	public String getLongName()
	{
		return longName;
	}
	
	public String toString()
	{
		return "Class "+longName;
	}
	
	public String toLongString()
	{
		return longName+" modifiers "+getModifiers();		
	}

	public IPackage getPackage() {
		return pkg;
	}

	public IProject getProject(){
		return project;
	}

	private void analyzeViaReflection() throws ClassNotFoundException
	{
		if(reflectionPeer==null)
		{
			CodeRepositoryInternal repo = (CodeRepositoryInternal)(project.getRepository());
			reflectionPeer = repo.getMavenClassLoader().loadClass(getLongName());
		}
	}

	/*
  SoftReference<Image> softImage = new SoftReference<Image>(null); 
    public Image getImage() throws IOException { 
    Image image = this.softImage.get(); 
    // Si la référence douce n'est pas valide 
    if (image==null) { 
      // On charge/recharge l'image 
      image = ImageIO.read(filename); 
      // Et on recrée une référence soft : 
      this.softImage = new SoftReference<Image>(image); 
    } 
    return image; 
  }*/
	
	//TODO V1.2 Améliorer : garder le zip ouvert en mémoire d'une classe à l'autre dans une soft reference ?
	private void analyzeViaBCEL() throws IOException{
		if(bcelPeer==null || bcelPeer.get()==null)
		{
			ClassParser cp= new ClassParser(zip_file,file_name);
			bcelPeer = new SoftReference<JavaClass>(cp.parse());
		};
	}

	public ImmutableNamedList<IField> getDeclaredFields() {
		try
		{
			analyzeViaReflection();
			Field[] f = reflectionPeer.getDeclaredFields();
			List<IField> lst = new ArrayList<IField>();
			for(Field fi : f)
			{
//				String fn = fi.getName();
//				Class t = fi.getType();
//				Type gt = fi.getGenericType();
//				List<IClass> ct = new ArrayList<IClass>();
//				if(gt instanceof ParameterizedType)
//				{
//					ParameterizedType pt = (ParameterizedType)gt;
//					Type[] ata = pt.getActualTypeArguments();
//					for(Type ti:ata)
//					{
//						if(ti instanceof Class)
//						{
//						ct.add(wrap((Class)ti));
//						}
//					}
//				}
//				IClass t2 = wrap(t);
//				if(t2!=null)
					lst.add(new FieldInternal(getRepository(),this,/*fn,t2,wrapModifiers(fi.getModifiers()),*/fi/*,ct*/));
			}
			return new ImmutableNamedList(lst);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}
	
	public IField getFieldByName(String fn){
        IField f = getDeclaredFields().get(fn);
        if(f==null)
        {
        	IType p = this.getSuperClass();
        	if(p!=null)
        	{
//        		System.out.println("PF63 calling super "+p+" to find "+fn);
        		return p.getBaseType().getBaseClass().getFieldByName(fn);
        	}
        	return null;
        }
        else
        	return f;
	}

	public ImmutableSet<IClass> getInnerClasses() {
		return ((CodeRepositoryInternal)getRepository()).getInnerClasses(this);
	}

	public ImmutableSet<IType> getInterfaces() {
		try
		{
			analyzeViaReflection();

			Class[] itfs = reflectionPeer.getInterfaces();
			Set<IType> lst = new HashSet<IType>();
			for(Class itf : itfs)
			{
//				IClass ft = project.getRepository().getClassByName(itf.getName());
				IType ft = wrap(itf);
				lst.add(ft);
			}
			return new ImmutableSet<IType>(lst);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

	@Override
	public ImmutableList<IMethod> getDeclaredMethods() {
		try
		{
			analyzeViaReflection();

			Method[] m = reflectionPeer.getDeclaredMethods();
			List<IMethod> rc = new ArrayList<IMethod>();
//			System.out.println("PF66 "+this);
			for(Method mi : m)
			{
//				System.out.println("\tPF66 "+mi);
				IMethod imi = wrap(this,mi);
//				System.out.println("\tPF69 "+imi);
				rc.add(imi);
			}
			return new ImmutableList<IMethod>(rc);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return new ImmutableList<IMethod>(new ArrayList<IMethod>());
		}
	}

	public ImmutableList<IMethod> getConstructors() {
		try
		{
			analyzeViaReflection();

			Constructor[] m = reflectionPeer.getConstructors();
			List<IMethod> rc = new ArrayList<IMethod>();
			for(Constructor mi : m)
			{
				//,wrapModifiers(mi.getModifiers()
				rc.add(wrap(this,mi));
			}
			return new ImmutableList<IMethod>(rc);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}
	

	public ImmutableSet<EModifier> getModifiers() {
		try
		{
			analyzeViaReflection();
			Set<EModifier> rc = wrapModifiers(reflectionPeer.getModifiers());
			if(reflectionPeer.isEnum())
			{
				rc.add(EModifier._enum);
			}
			if(reflectionPeer.isAnnotation())
			{
				rc.add(EModifier._annotation);
			}
			return new ImmutableSet<EModifier>(rc);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

	public String getName() {
		return shortName;
	}

	public IClass getParentClass() {
		if(parentClassName!=null && parentClass==null)
			parentClass = getRepository().getClassByName(parentClassName);
		return parentClass;
	}

	public ImmutableSet<IClass> getReferencedClasses() {
		try
		{
			analyzeViaBCEL();
			Set<String> hs=new HashSet<String>();
			ConstantPool cp = this.getPeer().getConstantPool();
            Constant[] ct = cp.getConstantPool();
            for (int j=0;j<ct.length;j++)
            {
                if(ct[j] instanceof ConstantClass)
                {
                    ConstantClass cc = (ConstantClass) ct[j];
                    String binary_cr=(String)cc.getConstantValue(cp);
                    
                    //beurk
                    if( binary_cr.indexOf("/")!=-1 && binary_cr.indexOf(";")==-1)
                        binary_cr="L"+binary_cr+";";
                    
                    String cr= BCELUtilities.parseBinaryType(binary_cr);
                    
                    if(cr!=null)
                    {
                        if(cr.trim().length()==0)
                            throw new RuntimeException(binary_cr+" => 0");
                        String cr1=cr.replace('/','.');
                            if(!hs.contains(cr1)&& cr1.compareTo(longName)!=0)
                            {
//                                _af.addReferencesClassConstant(split_pkg(cn),split_cls(cn),split_pkg(cr1),split_cls(cr1));
                                hs.add(cr1);
                            };
                    };
                }
                if(ct[j] instanceof ConstantNameAndType)
                {
                    ConstantNameAndType cc = (ConstantNameAndType) ct[j];
                    String binary_signature = (String)(cc.getSignature(cp));
                    String[] binary_cr = BCELUtilities.parseSignatureIncludingReturnType(binary_signature);
                    for (int k=0;k<binary_cr.length;k++)
                    {
                        String cr = binary_cr[k].replace('/','.');
                        if(!hs.contains(cr) && cr.compareTo(longName)!=0)
                        {
//                            _af.addReferencesClassConstant(split_pkg(cn),split_cls(cn),split_pkg(cr),split_cls(cr));
                            hs.add(cr);
                        };
                    }
                }
            };
            Set<IClass> set = new HashSet<IClass>();
            for(String cni:hs)
            {
            	if(! cni.endsWith(":primitive"))
            	{
            		//TODO V1.1 Pourquoi pas un parsing de BCEL directement en Type ? serait plus clean
            		if(cni.contains(":array"))
            		{
            			//TODO V1.2 gérer les dimensions
            			cni=cni.substring(0,cni.length()-":array[1]".length());
            		}
            		try
            		{
	            	IClass ft = project.getRepository().getClassByName(cni);
	            	set.add(ft);
            		}
            		catch(Throwable t)
            		{
            			t.printStackTrace();
            		}
            	}
            }
//            System.out.println("PIF07 "+hs);
            return new ImmutableSet<IClass>(set);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

	@Override
	public IType getSuperClass() {
		try
		{
			analyzeViaReflection();

//			Class zuper = reflectionPeer.getSuperclass();
			Type t = reflectionPeer.getGenericSuperclass();
//			if(zuper!=null)
			if(t!=null)
			{
//			IClass ft = project.getRepository().getClassByName(zuper.getName());
				IType ft = wrap(t);
			if(ft==null)
				System.err.println("PF45 Could not resolve super "+t);
			return ft;
			};
			return null;
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

	/*
	public FList<IClass> getActualTypeArgumentsInSuperclass() {
		try
		{
		analyzeViaReflection();
		List<IClass> ct = new ArrayList<IClass>();
		Type rt = reflectionPeer.getGenericSuperclass();
		if(rt instanceof ParameterizedType)
		{
			ParameterizedType pt = (ParameterizedType)rt;
			Type[] ata = pt.getActualTypeArguments();
			for(Type ti:ata)
			{
				if(ti instanceof Class)
				{
				ct.add(wrap((Class)ti));
				}
			}
		}
		return new FList<IClass>(ct);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}
*/
	 JavaClass getPeer() throws IOException {
		 JavaClass rc = null;
		 if(bcelPeer!=null)
		    rc=bcelPeer.get();
		    if(rc==null)
		    {
			    this.analyzeViaBCEL();
			    rc=bcelPeer.get();
		    };
		 return rc;
	}

	 public boolean equals(Object o)
	 {
		 IClass other = (IClass)o;
		 return other.getLongName().compareTo(longName)==0;
	 }
	 
	 public int hashCode()
	 {
		 return longName.hashCode();
	 }

	 private IMethod getConstructorByTypes(String[] pt)
	 {
		 StringBuffer sb=new StringBuffer();
		 loop: for(IMethod m : getConstructors())
		 {
				 sb.append("\tTrying "+m+"\n");
				 boolean match = true;
				 ImmutableList<IType> pti = m.getParameterTypes();

				 if(getParentClass()!=null && !getModifiers().contains(EModifier._static))
				 {
					 //Le constructeur d'une inner-classe non statique doit commencer par la classe englobante
					 List<IType> pti2 = new ArrayList<IType>();
					 pti2.add(TypeInternal.newInstance(getRepository(),m.getParentClass()));
					 for(IType c : pti)
					 {
						 pti2.add(c);
					 }
					 pti=new ImmutableList<IType>(pti2);
				 }

				 if(pti.count()!=pt.length)
				 {
					 sb.append("\t\tParameter count mismatch ("+pti.count()+" vs "+pt.length+")\n");
					 continue loop;
				 }
				 for(int i=0;i<pt.length;i++)
				 {
					 if(!(pti.get(i).getBaseClass() instanceof AnyClass) && pti.get(i).getLongName().compareTo(pt[i])!=0)
					 {
						 sb.append("\t\t"+m+" Parameter #"+i+" type mismatch "+ pti.get(i).getLongName()+ " vs "+pt[i]+" \n");
						 continue loop;						 
					 }
				 }
				 return m;
		}
		 throw new CouldNotResolveMethodException(this.longName, "<init>", pt, sb.toString());
	 }
	 
	 //TODO V1.1 Créer un index (dans une soft reference) des méthodes de chaque classe pour accéler la recherche
	 public IMethod getMethodByNameAndTypes(String mn, String[] pt) {
		 if(mn.compareTo("<init>")==0)
			 return getConstructorByTypes(pt);
		 
		 StringBuffer sb=new StringBuffer();
		 loop: for(IMethod m : getDeclaredMethods())
		 {
			 if(m.getName().compareTo(mn)==0)
			 {
				 sb.append("\tTrying "+m+"\n");
				 boolean match = true;
				 ImmutableList<IType> pti = m.getParameterTypes();
				 if(pti.count()!=pt.length)
				 {
					 sb.append("\t\tParameter count mismatch ("+pti.count()+" vs "+pt.length+")\n");
					 continue loop;
				 }
				 for(int i=0;i<pt.length;i++)
				 {
					 if(!(pti.get(i).getBaseClass() instanceof AnyClass) && pti.get(i).getLongName().compareTo(pt[i])!=0)
					 {
						 sb.append("\t\t"+m+" Parameter #"+i+" type mismatch "+ pti.get(i).getLongName()+ " vs "+pt[i]+" \n");
						 continue loop;						 
					 }
				 }
				 return m;
			 }
//				 else
//					 sb.append("Name mismatch with "+m+"\n");
		 };
		 		 
		 if(this.getModifiers().contains(EModifier._interface))
		 {
			 for(IType itf : getInterfaces())
			 {
				 sb.append("Trying "+itf+"\n");
				 try
				 {
					 IMethod m = itf.getBaseType().getBaseClass().getMethodByNameAndTypes(mn, pt);
					 if(m!=null)
						 return m;
				 }
				 catch(CouldNotResolveMethodException ex)
				 {
					 continue;
				 }
			 }
			 throw new CouldNotResolveMethodException(this.longName, mn, pt, sb.toString());
		 }
		 else if(getSuperClass()!=null)
		 {
//			 System.err.println("PF41 Calling super "+getSuperClass()+" for "+mn);
			 try
			 {
				 IMethod m = getSuperClass().getBaseType().getBaseClass().getMethodByNameAndTypes(mn, pt);
				 return m;
			 }
			 catch(CouldNotResolveMethodException ex)
			 {
				 throw new CouldNotResolveMethodException(this.longName, mn, pt, sb.toString(),ex);
			 }
//			 System.err.println("PF41 Found "+mn);
		 }
		 else
			 throw new CouldNotResolveMethodException(this.longName, mn, pt, sb.toString());
	 }

	public ImmutableNamedMap<IAnnotation> getAnnotations() {
		try
		{
			analyzeViaReflection();
			List<IAnnotation> rc=new ArrayList<IAnnotation>();
			for(Annotation a:reflectionPeer.getDeclaredAnnotations())
			{
				rc.add(new AnnotationInternal(a));
			}
			return new ImmutableNamedMap<IAnnotation>(rc);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}

	public ImmutableList<IMethod> getMethodByName(String mn) {
		
		List<IMethod> rc = new ArrayList<IMethod>();
		
		 String reason=null;
		 StringBuffer sb=new StringBuffer();
		 if(mn.compareTo("<init>")!=0)
		 {
			 loop: for(IMethod m : getDeclaredMethods())
			 {
				 if(m.getName().compareTo(mn)==0)
				 {
					 rc.add(m);
				 }
//				 else
//					 sb.append("Name mismatch with "+m+"\n");
			 };
		 }
		 else
		 {
			 loop: for(IMethod m : getConstructors())
			 {
				 rc.add(m);
			}
		 }
		 
		 		 
		 if(this.getModifiers().contains(EModifier._interface))
		 {
			 for(IType itf : getInterfaces())
			 {
				 sb.append("Trying "+itf+"\n");
				 try
				 {
					 for(IMethod m : itf.getBaseType().getBaseClass().getMethodByName(mn))
						 rc.add(m);
				 }
				 catch(CouldNotResolveMethodException ex)
				 {
					 continue;
				 }
			 }
			 throw new CouldNotResolveMethodException(this.longName, mn, null,sb.toString());
		 }
		 else if(getSuperClass()!=null)
		 {
//			 System.err.println("PF41 Calling super "+getSuperClass()+" for "+mn);
			 try
			 {
				 for(IMethod m: getSuperClass().getBaseType().getBaseClass().getMethodByName(mn))
				 {
					 rc.add(m);
				 }
			 }
			 catch(CouldNotResolveMethodException ex)
			 {
				 throw new CouldNotResolveMethodException(getSuperClass().getLongName(), mn, null, sb.toString(),ex);
			 }
//			 System.err.println("PF41 Found "+mn);
		 }
		 return new ImmutableList<IMethod>(rc);
	}
}
