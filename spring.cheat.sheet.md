# Spring cheat sheet

Below is the full set of configuration properties for usage of [xml-formatter] in Spring.

	<bean class="com.greenbird.xml.prettyprinter.PrettyPrinterFactory" name="prettyPrinterFactory">
		<!-- configure properties -->
		<!-- default indentation is a single tab -->
		<property name="indentationCharacter" value=" " />
		<property name="indentationMultiplier" value="2" />

		<!-- add prune filters, i.e. remove subtrees -->
		<property name="pruneFilters">
			<list>
				<value>/Envelope/Body/performLogMessageRequest/*</value>
				<!-- add more values here -->
			</list>
			
		</property>
		<!-- add anonymize filters, i.e. insert [*****] for certain values, like passwords, which should not be logged -->
		<property name="anonymizeFilters">
			<list>
				<value>/Envelope/Body/performLogMessageResponse/*</value>
				<!-- add more values here -->
			</list>
		</property>
		
		<!-- limit size of nodes, for example for embedded base64-content -->
		<property name="maxTextNodeLength" value="1024" />
		<property name="maxCDATANodeLength" value="1024" />

		<!-- pretty-print 'inner XML'  --> 		
		<property name="prettyPrintCData" value="true" />
		<property name="prettyPrintComments" value="true" />
		<property name="prettyPrintTextNodes" value="true" />
		
		<!-- do not fail on 'inner XML' non-wellformed  -->
		<property name="robustness" value="true" />
		
		<!-- keep XML declaration (default is false) -->
		<property name="xmlDeclaration" value="true" />

		<!-- for documents which already contain ignorable whitespace (cannot be used with filters) --> 		
		<property name="ignoreWhitespace" value="false" />
	</bean>
	
	<bean id="prettyPrinter" factory-bean="prettyPrinterFactory" factory-method="newPrettyPrinter" />

Then reference the created bean in normal way like for example

	<cxf:inInterceptors>
		<bean class="com.greenbird.xmlformatter.cxf.LoggingInInterceptor">
			<property name="prettyLogging" value="true" />
			<property name="prettyPrinter" ref="prettyPrinter" />
		</bean>
	</cxf:inInterceptors>

In case you want different behaviour for in- and output logging, go with

    <!-- configure logging -->
	<spring:bean class="com.greenbird.xml.prettyprinter.PrettyPrinterFactory" name="inputPrettyPrinterFactory">
		<!-- configure properties -->
	</spring:bean>    	
	
	<bean id="inputPrettyPrinter" factory-bean="inputPrettyPrinterFactory" factory-method="newPrettyPrinter" />
	
	<spring:bean class="com.greenbird.xml.prettyprinter.PrettyPrinterFactory" name="outputPrettyPrinterFactory">
		<!-- configure properties -->
	</spring:bean>
	
	<bean id="outputPrettyPrinter" factory-bean="outputPrettyPrinterFactory" factory-method="newPrettyPrinter" />
	
Alternatively, construct the pretty-printer instance directly;

	<cxf:inInterceptors>
	    <spring:bean class="com.greenbird.xmlformatter.mule.cxf.XMLLoggingInInterceptor">
	        <spring:property name="name" value="LoggerServiceReceived"/>
	        <spring:property name="prettyPrinter" value="#{inputPrettyPrinterFactory.newPrettyPrinter()}"/>
	    </spring:bean>
	</cxf:inInterceptors>



[xml-formatter]:	   https://github.com/greenbird/xml-formatter-core



