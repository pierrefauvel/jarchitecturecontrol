package org.jarco.code.internal.bcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Utility;

public class BCELUtilities {
    static String[] af2mod ( AccessFlags jc, boolean _class)
    {
            ArrayList af = new ArrayList();
            if(jc.isAbstract()) af.add("abstract");
            if(jc.isFinal()) af.add("final");
            if(jc.isInterface()) af.add("interface");
            if(jc.isNative()) af.add("native");
            if(jc.isPrivate()) af.add("private");
            if(jc.isProtected()) af.add("protected");
            if(jc.isPublic()) af.add("public");
            if(jc.isStatic()) af.add("static");
            if(jc.isStrictfp()) af.add("strictfp");
            if(!_class)
                if(jc.isSynchronized()) af.add("synchronized");
            if(jc.isTransient()) af.add("transient");
            if(jc.isVolatile()) af.add("volatile");
            return (String[])(af.toArray(new String[]{}));
    }

    static boolean isSynthetic(Attribute[] atts)
    {
        for (int i=0;i<atts.length;i++)
        {
            if(atts[i] instanceof Synthetic)
            {
                return true;
            }
        };
        return false;
    }

    static InnerClasses getInnerClassAttribute(Attribute[] atts)
    {
        for (int i=0;i<atts.length;i++)
        {
            if(atts[i] instanceof InnerClasses)
            {
                return (InnerClasses)(atts[i]);
            };
        };
        return null;
    }    

    
    public static final String PRIMITIVE_TYPE_VOID = "Void:primitive";
    public static final String PRIMITIVE_TYPE_BYTE = "Byte:primitive";
    public static final String PRIMITIVE_TYPE_BOOLEAN = "Boolean:primitive";
    public static final String PRIMITIVE_TYPE_SHORT = "Short:primitive";
    public static final String PRIMITIVE_TYPE_INT = "Integer:primitive";
    public static final String PRIMITIVE_TYPE_LONG = "Long:primitive";
    public static final String PRIMITIVE_TYPE_FLOAT = "Float:primitive";
    public static final String PRIMITIVE_TYPE_DOUBLE = "Double:primitive";
    public static final String PRIMITIVE_TYPE_CHAR = "Character:primitive";
    
    
    private static Map<String,String> hm_primitives=new HashMap<String,String>();
    static
    {
        hm_primitives.put("void",PRIMITIVE_TYPE_VOID);
        hm_primitives.put("byte",PRIMITIVE_TYPE_BYTE);
        hm_primitives.put("boolean",PRIMITIVE_TYPE_BOOLEAN);
        hm_primitives.put("short",PRIMITIVE_TYPE_SHORT);
        hm_primitives.put("int",PRIMITIVE_TYPE_INT);
        hm_primitives.put("long",PRIMITIVE_TYPE_LONG);
        hm_primitives.put("float",PRIMITIVE_TYPE_FLOAT);
        hm_primitives.put("double",PRIMITIVE_TYPE_DOUBLE);
        hm_primitives.put("char",PRIMITIVE_TYPE_CHAR);
    }

  public static String[] parseSignatureIncludingReturnType ( String sign)
  {
	 List lst=new ArrayList();
    parseSignatureIncludingReturnType(lst,sign);
    return (String[])(lst.toArray(new String[]{}));
  }

  public static String[] parseSignatureExcludingReturnType ( String sign)
  {
		 List lst=new ArrayList();
    parseSignatureExcludingReturnType(lst,sign);
    return (String[])(lst.toArray(new String[]{}));
  }

public static String parseBinaryType(String type)
{
    List hs=new ArrayList();
    addParsedType(hs,Utility.signatureToString(type,false));
     if(hs.size()==1)
        return (String)(hs.iterator().next());
    return null;
}
  /*
  public static boolean isArray(String type)
  {
    return type.charAt(0)=='[';
  }
  
  public static boolean isPrimitive(String type)
  {
    return hm_primitives.containsKey(Utility.signatureToString(type,false));
  }
  public static String parseBinaryPrimitive(String type) 
  {
  	String rc=Utility.signatureToString(type,false);
  	if(!hm_primitives.containsKey(rc))
  		throw new RuntimeException(type+" is not primitive");
  	return rc;
  }

    public static boolean isVoid(String type)
    {
        return Utility.signatureToString(type,false).compareTo("void")==0;
    }
  */
  
  private static void parseSignatureIncludingReturnType(List lst, String sign)
  {
    if(sign.length()==0)
        return;
    if(sign.charAt(0)=='(')
    {
        String rt = Utility.methodSignatureReturnType(sign,false);
        String[] pt = Utility.methodSignatureArgumentTypes(sign,false);
        addParsedType(lst,rt);
        for (int i=0;i<pt.length;i++)
            addParsedType(lst,pt[i]);
    }
    else
    {
        addParsedType(lst,Utility.signatureToString(sign,false));
    };
  }

  private static void parseSignatureExcludingReturnType(List lst, String sign)
  {
    if(sign.length()==0)
        return;
    if(sign.charAt(0)=='(')
    {
//        String rt = Utility.methodSignatureReturnType(sign,false);
        String[] pt = Utility.methodSignatureArgumentTypes(sign,false);
 //       addParsedType(lst,rt);
        for (int i=0;i<pt.length;i++)
            addParsedType(lst,pt[i]);
    }
    else
    {
        addParsedType(lst,Utility.signatureToString(sign,false));
    };
  }

  private static void addParsedType(List lst, String str_type)
  {
    int idx=str_type.indexOf("[");
//TODO V1.2 gérér les dimensions
    if(idx!=-1)
        str_type=str_type.substring(0,idx)+":array[1]";
    if(hm_primitives.containsKey(str_type))
    {
    	lst.add(hm_primitives.get(str_type));
    }
    else
    	lst.add(str_type);
  }

}
