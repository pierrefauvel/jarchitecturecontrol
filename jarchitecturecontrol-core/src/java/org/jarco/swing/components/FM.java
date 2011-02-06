package org.jarco.swing.components;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface FM {
	enum kind { treenode, component, injected, ignore };
	kind kind();
}
