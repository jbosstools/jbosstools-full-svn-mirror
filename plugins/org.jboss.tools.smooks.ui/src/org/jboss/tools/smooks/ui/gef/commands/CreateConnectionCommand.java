package org.jboss.tools.smooks.ui.gef.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

/**
 * l�ߴ�������
 * 
 * @author wangpeng created 2007-4-5 14:51:00
 *
 */
public class CreateConnectionCommand extends Command {
	
	/**
	 * ÿһ����϶�����Դ��ݶ���ڵ��Ŀ����ݶ���ڵ���ɵ�.
	 * ��}��ڵ�����Ͷ���List��
	 */
	public static Map listComponents = new HashMap();

	private IConnectableModel source;
	private IConnectableModel target;
	
	/**
	 * t��ģ��
	 */
	private LineConnectionModel connection;
	
	/**
	 * �����ж��Ƿ��ܹ�ִ��l��
	 */
	public boolean canExecute() {

		if ( !validate() ) {
			return false;
		}
		
		return true;
	}
	
	public void execute() {
		connection = new LineConnectionModel();
		connection.setSource(source);
		connection.setTarget(target);
		connection.connect();
	}

	public void setConnection(Object model) {
		this.connection = (LineConnectionModel)model;
	}

	public void setSource(Object model) {
		this.source = (IConnectableModel)model;
	}

	public void setTarget(Object model) {
		this.target = (IConnectableModel)model;
	}
	
	public void undo() {
		connection.detachSource();
		connection.detachTarget();
	}

	/**
	 * ��֤l�ӵ�Դ��Ŀ���Ƿ���Խ���l��
	 * <p>
	 * ��֤����
	 * <ul>
	 * <li>1��
	 * </ul>
	 * </p>
	 * 
	 * @see com.tongtech.ti.tisd.project.converter.ui.util.validity.Validator#validate()
	 */
	public boolean validate() {
		return true;
	}
	
	/**
	 * @return ���� source��
	 */
	public IConnectableModel getSource() {
		return source;
	}

	/**
	 * @return ���� target��
	 */
	public IConnectableModel getTarget() {
		return target;
	}
}