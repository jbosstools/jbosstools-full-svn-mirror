/**
 * 
 */
package org.jboss.tools.smooks.javabean.analyzer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.analyzer.ResolveCommand;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart
 *
 */
public class Java2JavaResolveCommand extends ResolveCommand {

	private AbstractStructuredDataModel sourceModel;
	
	private AbstractStructuredDataModel targetModel;
	
	private List<LineConnectionModel> disconnectionModel = new ArrayList<LineConnectionModel>();
	
	public void addDisconnectionModel(LineConnectionModel line){
		disconnectionModel.add(line);
	}

	public AbstractStructuredDataModel getSourceModel() {
		return sourceModel;
	}

	public void setSourceModel(AbstractStructuredDataModel sourceModel) {
		this.sourceModel = sourceModel;
	}

	public AbstractStructuredDataModel getTargetModel() {
		return targetModel;
	}

	public void setTargetModel(AbstractStructuredDataModel targetModel) {
		this.targetModel = targetModel;
	}

	public Java2JavaResolveCommand(
			SmooksConfigurationFileGenerateContext context) {
		super(context);
	}

	@Override
	public void execute() throws Exception {
		SmooksConfigurationFileGenerateContext context = getContext();
		if(context == null) throw new RuntimeException("Smooks generated context is NULL");
		if(sourceModel != null && targetModel != null){
			LineConnectionModel connectionModel = new LineConnectionModel();
			connectionModel
					.setSource((IConnectableModel) sourceModel);
			connectionModel
					.setTarget((IConnectableModel) targetModel);
			connectionModel.connect();
			return;
		}
		if(!disconnectionModel.isEmpty()){
			for (Iterator<LineConnectionModel> iterator = disconnectionModel.iterator(); iterator.hasNext();) {
				LineConnectionModel connection = (LineConnectionModel) iterator.next();
				connection.disConnect();
			}
			return;
		}
	}
}
