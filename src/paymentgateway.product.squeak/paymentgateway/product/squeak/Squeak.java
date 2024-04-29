package paymentgateway.product.squeak;

import java.util.ArrayList;

import vmj.routing.route.VMJServer;
import vmj.routing.route.Router;
import vmj.hibernate.integrator.HibernateUtil;
import org.hibernate.cfg.Configuration;

FreeMarker template error (DEBUG mode; use RETHROW in production!):
The following has evaluated to null or missing:
==> imports  [in template "ProductClass.ftl" at line 10, column 8]

----
Tip: If the failing expression is known to legally refer to something that's sometimes null or missing, either specify a default value like myOptionalVar!myDefault, or use <#if myOptionalVar??>when-present<#else>when-missing</#if>. (These only cover the last step of the expression; to cover the whole expression, use parenthesis: (myOptionalVar.foo)!myDefault, (myOptionalVar.foo)??
----

----
FTL stack trace ("~" means nesting-related):
	- Failed at: #list imports as import  [in template "ProductClass.ftl" at line 10, column 1]
----

Java stack trace (for programmers):
----
freemarker.core.InvalidReferenceException: [... Exception message was already printed; see it above ...]
	at freemarker.core.InvalidReferenceException.getInstance(InvalidReferenceException.java:134)
	at freemarker.core.Expression.assertNonNull(Expression.java:249)
	at freemarker.core.IteratorBlock.acceptWithResult(IteratorBlock.java:104)
	at freemarker.core.IteratorBlock.accept(IteratorBlock.java:94)
	at freemarker.core.Environment.visit(Environment.java:347)
	at freemarker.core.Environment.visit(Environment.java:353)
	at freemarker.core.Environment.process(Environment.java:326)
	at freemarker.template.Template.process(Template.java:383)
	at de.ovgu.featureide.core.winvmj.templates.TemplateRenderer.write(TemplateRenderer.java:66)
	at de.ovgu.featureide.core.winvmj.templates.TemplateRenderer.render(TemplateRenderer.java:37)
	at de.ovgu.featureide.core.winvmj.WinVMJComposer.composeProduct(WinVMJComposer.java:96)
	at de.ovgu.featureide.core.winvmj.WinVMJComposer$1.execute(WinVMJComposer.java:76)
	at de.ovgu.featureide.core.winvmj.WinVMJComposer$1.execute(WinVMJComposer.java:1)
	at de.ovgu.featureide.fm.core.job.Executer.execute(Executer.java:44)
	at de.ovgu.featureide.fm.core.job.LongRunningJob.work(LongRunningJob.java:48)
	at de.ovgu.featureide.fm.core.job.AbstractJob.run(AbstractJob.java:122)
	at org.eclipse.core.internal.jobs.Worker.run(Worker.java:63)
