<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<html>
<head>
</head>
<body>

<f:view>
	<f:subview id="subview">
		<h1><h:outputText value="Selection" /></h1>

		<h:form id="selection">

			<h3><h:outputText value="JSF's TAGS" /></h3>
			<h:commandButton value="commandButton" />
			</b>
			<h:commandLink value="commandLink" />
			</b>
			<h:dataTable value="data">

				<h:column>
					<f:facet name="header">
						<h:outputText value="Last Name" />
					</f:facet>

					<h:outputText value="Dupont" />

					<f:facet name="footer">
						<h:outputText value="footer" />
					</f:facet>

				</h:column>

			</h:dataTable>

			</b>
			<h:graphicImage value="/img/image.jpeg" />
			</b>
			<h:inputHidden value="inputHidden" />
			</b>
			<h:inputSecret value="inputSecret" />
			</b>
			<h:inputText value="inputText" />
			</b>
			<h:inputTextarea value="inputTextArea" />
			</b>
			<h:message for="name" />
			</b>
			<h:messages style="color: red" />
			</b>
			<h:outputText value="outputFormat" />
			</b>
			<h:outputLabel value="outputLabel" />
			</b>
			<h:outputLink value="www.exadel.com">
			outputLink
		</h:outputLink>
			</b>
			<h:outputText value="outputText" />
			</b>
			<h:panelGrid columns="2">

				<h:panelGroup>
					<h:outputText value="1" />
					<h:outputText value="2" />
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="3" />
					<h:outputText value="4" />
				</h:panelGroup>
			</h:panelGrid>
			</b>
			<h:selectBooleanCheckbox value="false" />
			<h:selectManyCheckbox value="someValue">
				<f:selectItem itemLabel="check1" />
				<f:selectItems value="someValue" />
			</h:selectManyCheckbox>
			<h:selectManyListbox value="someValue">
				<f:selectItem itemLabel="value1" itemValue="value1" />
				<f:selectItem itemLabel="value2" itemValue="value2" />
			</h:selectManyListbox>
			</b>
			<h:selectManyMenu value="someValue">
				<f:selectItem itemLabel="value1" itemValue="value1" />
				<f:selectItem itemLabel="value2" itemValue="value2" />
			</h:selectManyMenu>
			</b>
			<h:selectOneListbox value="someValue">
				<f:selectItem itemLabel="value1" itemValue="value1" />
				<f:selectItem itemLabel="value2" itemValue="value2" />
			</h:selectOneListbox>
			</b>
			<h:selectOneMenu value="someValue">
				<f:selectItem itemLabel="value2" itemValue="value2" />

			</h:selectOneMenu>
			<h:selectOneRadio>
				<f:selectItem itemLabel="value1" />
				<f:selectItem itemLabel="value2" />
			</h:selectOneRadio>
			<f:verbatim>
				verbatim
			</f:verbatim>
			<h3><h:outputText value="RICHFACES TAGS" /></h3>
	<!-- Virtual earth component -->
	<rich:virtualEarth>
		<rich:toolTip>
			<h:outputText value="This is virtual earth component" />
		</rich:toolTip>
	</rich:virtualEarth>

	<!-- Tree component -->
	<rich:tree style="width:300px">
		<!-- Tree Nodes component -->
		<rich:treeNodesAdaptor>
			<!-- Tree Node component -->
			<rich:treeNode type="artist">
				<h:outputText value="Node1" />
			</rich:treeNode>
			<rich:treeNode type="album">
				<h:outputText value="Node2" />
			</rich:treeNode>
		</rich:treeNodesAdaptor>	
				<!-- Recursive TreeNodes component -->
		<rich:recursiveTreeNodesAdaptor>
			<rich:treeNode type="song">
				<h:outputText value="Leaf1" />
			</rich:treeNode>
			<rich:treeNode type="song">
				<h:outputText value="Leaf2" />
			</rich:treeNode>
		</rich:recursiveTreeNodesAdaptor>
        </rich:tree>

	<!-- Separator component -->
	<rich:separator />

	<!-- Toggle Panel component -->
	<rich:togglePanel value="#{toggleBean.skinChooserState1}"
		switchType="client" stateOrder="closed,tip1, tip2,tip3">

		<f:facet name="closed">
			<!-- Toggle Control component-->
			<rich:toggleControl>
				<h:graphicImage id="pic" style="border-width:0"
					value="/pictures/clickme.gif" />
			</rich:toggleControl>
		</f:facet>

		<f:facet name="tip1">
			<ui:include src="/pages/tipBlock.xhtml">
				<ui:param name="tip"
					value="rich:toggleControl might bre located inside of outside
             of the rich:toogleControl it works for. In case of outside location, the control
             attribute 'for' should refer to the toggle panel id." />
				<ui:param name="next" value="tip2" />
			</ui:include>
		</f:facet>

		<f:facet name="tip2">
			<ui:include src="/pages/tipBlock.xhtml">
				<ui:param name="tip"
					value="Attribute initialState defines the first state appear
             when the page is loaded. If this attribute is not specified, the first state
             mentioned in the attribute stateOrder will be the first" />
				<ui:param name="previous" value="tip1" />
				<ui:param name="next" value="tip3" />
			</ui:include>
		</f:facet>

		<f:facet name="tip3">
			<ui:include src="/pages/tipBlock.xhtml">
				<ui:param name="tip"
					value="Note that rich:toggleControl similar to
            h:commandLink for 'server' mode and smilar to a4j:commandLink for 'ajax' mode.
            Set immediate attribute to true if jsf form has other input field that might
            cause the validation error. Otherwize, the state switched will not perform" />
				<ui:param name="previous" value="tip2" />
			</ui:include>
		</f:facet>
	</rich:togglePanel>

	<!-- Tool Bar component -->
	<rich:toolBar id="bar" height="30">
		<!-- Tool Bar Group component -->
		<rich:toolBarGroup>
			<h:outputText value="Group1.1" />
			<h:outputText value="Group1.2" />
			<h:outputText value="Group1.3" />
		</rich:toolBarGroup>
		<!-- Tool Bar Group component -->
		<rich:toolBarGroup>
			<h:outputText value="Group2.1" />
			<h:outputText value="Group2.2" />
		</rich:toolBarGroup>
		<!-- Tool Bar Group component -->
		<rich:toolBarGroup location="right">
			<h:outputText value="Group3.1" />
			<h:outputText value="Group3.2" />
		</rich:toolBarGroup>
	</rich:toolBar>

	<!-- Suggestion Box component -->
	<h:inputText value="#{suggestionBox.property}" id="text" />
	<rich:suggestionbox id="suggestionBoxId" for="text" tokens=",["
		rules="#{suggestionBox.rules}"
		suggestionAction="#{suggestionBox.autocomplete}" var="result"
		fetchValue="#{result.text}" rows="#{suggestionBox.intRows}"
		first="#{suggestionBox.intFirst}" minChars="#{suggestionBox.minchars}"
		shadowOpacity="#{suggestionBox.shadowOpacity}"
		border="#{suggestionBox.border}" width="#{suggestionBox.width}"
		height="#{suggestionBox.height}"
		shadowDepth="#{suggestionBox.shadowDepth}"
		cellpadding="#{suggestionBox.cellpadding}">
		<h:column>
			<h:outputText value="#{result.text}" />
		</h:column>
	</rich:suggestionbox>

	<!-- Tab panel component -->
	<rich:tabPanel>
		<!-- Tab component -->
		<rich:tab label="First">
            Here is tab #1
        </rich:tab>
		<rich:tab label="Second">
            Here is tab #2
        </rich:tab>
		<rich:tab label="Third">
            Here is tab #3
        </rich:tab>
	</rich:tabPanel>

	<!-- Spacer component -->
	<rich:spacer width="720" height="10" />

	<!-- Simple Toggle Panel component -->
	<rich:simpleTogglePanel switchType="client"
		label="Test simple toggle panel">
        This is simple toggle panel
    </rich:simpleTogglePanel>

	<!-- Scrollable Data Table component -->
	<rich:scrollableDataTable rowKeyVar="rkv" frozenColCount="1"
		height="200px" width="700px" rows="10" columnClasses="col"
		sortMode="single">
		<rich:column>
			<f:facet name="header">
				<h:outputText styleClass="headerText" value="Car" />
			</f:facet>
			<h:outputText value="Car1" />
		</rich:column>
		<rich:column>
			<f:facet name="header">
				<h:outputText styleClass="headerText" value="Model" />
			</f:facet>
			<h:outputText value="Model1" />
		</rich:column>
	</rich:scrollableDataTable>

	<!-- Panel Menu component -->
	<h:panelGrid columns="2" columnClasses="cols" width="100%">
		<rich:panelMenu style="width:200px" mode="ajax"
			iconExpandedGroup="disc" iconCollapsedGroup="disc"
			iconExpandedTopGroup="chevronUp" iconGroupTopPosition="right"
			iconCollapsedTopGroup="chevronDown" iconCollapsedTopPosition="right">
			<!-- Panel Menu Group component -->
			<rich:panelMenuGroup label="Group 1">
				<rich:panelMenuItem label="Item 1.1"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 1.1" />
				</rich:panelMenuItem>
				<!-- Panel Menu Item component -->
				<rich:panelMenuItem label="Item 1.2"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 1.2" />
				</rich:panelMenuItem>
				<rich:panelMenuItem label="Item 1.3"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 1.3" />
				</rich:panelMenuItem>
			</rich:panelMenuGroup>
			<rich:panelMenuGroup label="Group 2">
				<rich:panelMenuItem label="Item 2.1"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 2.1" />
				</rich:panelMenuItem>
				<rich:panelMenuItem label="Item 2.2"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 2.2" />
				</rich:panelMenuItem>
				<rich:panelMenuItem label="Item 2.3"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 2.3" />
				</rich:panelMenuItem>
				<rich:panelMenuGroup label="Group 2.4">
					<rich:panelMenuItem label="Item 2.4.1"
						action="#{panelMenu.updateCurrent}">
						<f:param name="current" value="Item 2.4.1" />
					</rich:panelMenuItem>
					<rich:panelMenuItem label="Item 2.4.2"
						action="#{panelMenu.updateCurrent}">
						<f:param name="current" value="Item 2.4.2" />
					</rich:panelMenuItem>
					<rich:panelMenuItem label="Item 2.4.3"
						action="#{panelMenu.updateCurrent}">
						<f:param name="current" value="Item 2.4.3" />
					</rich:panelMenuItem>
				</rich:panelMenuGroup>
				<rich:panelMenuItem label="Item 2.5"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 2.5" />
				</rich:panelMenuItem>
			</rich:panelMenuGroup>
			<rich:panelMenuGroup label="Group 3">
				<rich:panelMenuItem label="Item 3.1"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 3.1" />
				</rich:panelMenuItem>
				<rich:panelMenuItem label="Item 3.2"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 3.2" />
				</rich:panelMenuItem>
				<rich:panelMenuItem label="Item 3.3"
					action="#{panelMenu.updateCurrent}">
					<f:param name="current" value="Item 3.3" />
				</rich:panelMenuItem>
			</rich:panelMenuGroup>
		</rich:panelMenu>
	</h:panelGrid>

	<!-- Panel component -->
	<rich:panel>
		<f:facet name="header">
        	This rich panel component
        </f:facet>
        	Test rich panel component
    </rich:panel>


	<!-- Panel bar component -->
	<rich:panelBar height="50" width="500">
		<rich:panelBarItem label="Panel bar item 1">
            Test panel bar item component
        </rich:panelBarItem>
		<rich:panelBarItem label="Panel bar item 2">
            Test panel bar item component
        </rich:panelBarItem>
	</rich:panelBar>

	<!-- Paint 2D component -->
	<rich:paint2D id="painter" width="300" height="120" data="#{paintData}"
		format="png" paint="#{paintBean.paint}" />
	<rich:spacer width="720" height="10" />

	<!-- Calendar -->
	<rich:calendar />

	<!-- Data Grid -->
	<rich:panel>
		<f:facet name="header">
			<h:outputText value="Car Store"></h:outputText>
		</f:facet>
		<h:form>
			<rich:dataGrid columns="2" elements="12">
				<rich:panel>
					<f:facet name="header">
						<h:outputText value="Car"></h:outputText>
					</f:facet>
					<h:panelGrid columns="2">
						<h:outputText value="Price:"></h:outputText>
						<h:outputText value="30000" />
						<h:outputText value="Mileage:"></h:outputText>
						<h:outputText value="345345345" />
						<h:outputText value="VIN:"></h:outputText>
						<h:outputText value="HKFFGHGKHJKLJ" />
						<h:outputText value="Stock:"></h:outputText>
						<h:outputText value="GHKFGH" />
					</h:panelGrid>
				</rich:panel>
			</rich:dataGrid>
		</h:form>
	</rich:panel>

	<!--  DataDefenitionList -->
	<rich:dataDefinitionList rows="5">
		<h:outputText value="testDefinitionList" />
	</rich:dataDefinitionList>

	<!-- Data Filter Slider -->
	<rich:dataFilterSlider startRange="1" endRange="1000" increment="100"
		trailer="true" />

	<!-- Data List -->
	<rich:dataList rows="5">
		<h:outputText value="testList" />
	</rich:dataList>

	<!-- Data Ordered List -->
	<rich:dataOrderedList rows="5">
		<h:outputText value="testOrderedList" />
	</rich:dataOrderedList>

	<!-- Data Table -->
	<rich:dataTable onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
		onRowMouseOut="this.style.backgroundColor='#F2F2F2'" cellpadding="0"
		cellspacing="0" width="700" border="0" value="table">

		<f:facet name="header">
			<rich:columnGroup>
				<rich:column rowspan="2">
					<rich:spacer />
				</rich:column>
				<rich:column colspan="3">
					<h:outputText value="Expenses" />
				</rich:column>
				<rich:column rowspan="2">
					<h:outputText value="subtotals" />
				</rich:column>
				<rich:column breakBefore="true">
					<h:outputText value="Meals" />
				</rich:column>
				<rich:column>
					<h:outputText value="Hotels" />
				</rich:column>
				<rich:column>
					<h:outputText value="Transport" />
				</rich:column>
			</rich:columnGroup>
		</f:facet>
		<rich:column colspan="5">
			<h:outputText value="Minsk" />
		</rich:column>

		<rich:subTable onRowMouseOver="this.style.backgroundColor='#F8F8F8'"
			onRowMouseOut="this.style.backgroundColor='F9F9F9'" value="SubTable">
			<rich:column>
				<h:outputText value="25-Aug-97"></h:outputText> 
			</rich:column>
			<rich:column>
				<h:outputText value="30"></h:outputText>
			</rich:column>
			<rich:column>
				<h:outputText value="30"></h:outputText>
			</rich:column>
			<rich:column>
				<h:outputText value="100"></h:outputText>
			</rich:column>
			<rich:column>
				<h:outputText value="20"></h:outputText>
			</rich:column>
		</rich:subTable>
		<f:facet name="footer">
			<rich:columnGroup>
				<rich:column><h:outputText value="Totals"></h:outputText></rich:column>
				<rich:column>
					<h:outputText value="30"></h:outputText>
				</rich:column>
				<rich:column>
					<h:outputText value="50"></h:outputText>
				</rich:column>
				<rich:column>
					<h:outputText value="10"></h:outputText>
				</rich:column>
				<rich:column>
					<h:outputText value="20"></h:outputText>
				</rich:column>
			</rich:columnGroup>
		</f:facet>
	</rich:dataTable>

	<!-- Data Scroller -->
	<rich:datascroller>
	</rich:datascroller>

	<!-- Drag and Drop -->
	<rich:dragIndicator id="indicator" />
	<h:form id="form">
		<h:panelGrid columnClasses="panelc" columns="4" width="100%">
			<rich:panel style="width:100px">
				<f:facet name="header">
					<h:outputText value="Source List" />
				</f:facet>
				<h:dataTable id="src" columns="1" value="#{dndBean.frameworks}"
					var="fm">
					<h:column>
						<a4j:outputPanel style="border:1px solid gray;padding:2px;"
							layout="block">
							<rich:dragSupport dragIndicator=":indicator"
								dragType="#{fm.family}" dragValue="#{fm}">
								<rich:dndParam name="label" value="#{fm.name}" />
							</rich:dragSupport>
							<h:outputText value="Test"></h:outputText>
						</a4j:outputPanel>
					</h:column>
				</h:dataTable>
			</rich:panel>
			<rich:panel styleClass="dropTargetPanel">
				<f:facet name="header">
					<h:outputText value="PHP Frameworks" />
				</f:facet>
				<rich:dropSupport id="php" acceptedTypes="PHP" dropValue="PHP"
					dropListener="#{eventBean.processDrop}" reRender="phptable, src">
				</rich:dropSupport>
				<h:dataTable id="phptable" columns="1"
					value="#{dndBean.containerPHP}" var="fm">
					<h:column>
						<h:outputText value="Test"></h:outputText>
					</h:column>
				</h:dataTable>
			</rich:panel>
			<rich:panel styleClass="dropTargetPanel">
				<f:facet name="header">
					<h:outputText value=".NET Frameworks" />
				</f:facet>
				<rich:dropSupport id="dnet" acceptedTypes="DNET" dropValue="DNET"
					dropListener="#{eventBean.processDrop}" reRender="dnettable, src">
				</rich:dropSupport>
				<h:dataTable id="dnettable" columns="1"
					value="#{dndBean.containerDNET}" var="fm">
					<h:column>
						<h:outputText value="Test"></h:outputText>
					</h:column>
				</h:dataTable>
			</rich:panel>
			<rich:panel styleClass="dropTargetPanel">
				<f:facet name="header">
					<h:outputText value="ColdFusion Frameworks" />
				</f:facet>
				<rich:dropSupport id="cf" acceptedTypes="CF" dropValue="CF"
					dropListener="#{eventBean.processDrop}" reRender="cftable, src">
				</rich:dropSupport>
				<h:dataTable id="cftable" columns="1" value="#{dndBean.containerCF}"
					var="fm">
					<h:column>
						<h:outputText value="Test"></h:outputText>
					</h:column>
				</h:dataTable>
			</rich:panel>
		</h:panelGrid>
		<a4j:commandButton action="#{dndBean.reset}" value="Start Over"
			reRender="src,phptable,cftable,dnettable" />
	</h:form>

	<!-- Effect -->
	<rich:panel id="fadebox" styleClass="box">
		<f:facet name="header">Fade Effect</f:facet>
		<rich:effect event="onclick" type="Fade" />
		<rich:effect event="onclick" for="fadebox" type="Appear"
			params="delay:3.0,duration:0.5" />
		<h:outputText value="Click to Activate" />
	</rich:panel>

	<!-- Google map -->
	<rich:gmap>
	</rich:gmap>

	<!-- Input Number Slider -->
	<rich:inputNumberSlider maxValue="50">
	</rich:inputNumberSlider>

	<!-- Input Number Spinner -->
	<rich:inputNumberSpinner minValue="1" maxValue="50">
	</rich:inputNumberSpinner>

	<!-- Insert -->
	<rich:insert></rich:insert>

	<!-- Drop Down Menu,Menu Group, Menu Item, Menu Separator -->
	<rich:dropDownMenu value="File">
		<rich:menuItem submitMode="ajax" value="New">
		</rich:menuItem>
		<rich:menuItem submitMode="ajax" value="Open" />
		<rich:menuGroup value="Save As...">
			<rich:menuItem submitMode="ajax" value="Text File" />
			<rich:menuItem submitMode="ajax" value="PDF File" />
		</rich:menuGroup>
		<rich:menuItem submitMode="ajax" value="Close" />
		<rich:menuSeparator id="menuSeparator11" />
		<rich:menuItem submitMode="ajax" value="Exit" />
	</rich:dropDownMenu>

	<!-- Message -->
	<rich:message>
	</rich:message>

	<!-- Messages -->
	<rich:messages>
	</rich:messages>

	<!-- Modal Panel -->
	<rich:modalPanel id="mp" minHeight="200" minWidth="450" height="200"
		width="500" zindex="2000">
		<f:facet name="header">
			<h:outputText value="Modal Panel Title" />
		</f:facet>
	</rich:modalPanel>
   
   <!-- componentControl -->
    <rich:componentControl for="mp" attachTo="link" operation="show" event="onclick"/>

			</h:form>
	</f:subview>
</f:view>
</body>
</html>