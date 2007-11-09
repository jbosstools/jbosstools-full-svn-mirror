@ echo ------ Building XML Objects.. ------
@ echo -- JWSDP_HOME = %JWSDP_HOME%
@ echo -- JBOSS_AOP_ECLIPSE = %JBOSS_AOP_ECLIPSE%
@ call %JWSDP_HOME%\jaxb\bin\xjc.bat -extension -d %JBOSS_AOP_ECLIPSE%\org.jboss.ide.eclipse.jdt.aop.core\generated -p org.jboss.ide.eclipse.jdt.aop.core.jaxb %JBOSS_AOP_ECLIPSE%\org.jboss.ide.eclipse.jdt.aop.core\aop-report.xsd %JBOSS_AOP_ECLIPSE%\org.jboss.ide.eclipse.jdt.aop.core\jboss-aop.xsd
@ echo ------ Done -------