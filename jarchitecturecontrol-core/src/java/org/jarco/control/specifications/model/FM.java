package org.jarco.control.specifications.model;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface FM {
	enum kind { treenode, component, injected, ignore };
	kind kind();
}
