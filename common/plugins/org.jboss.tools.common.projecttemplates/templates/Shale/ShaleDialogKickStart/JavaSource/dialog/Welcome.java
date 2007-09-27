/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package dialog;

/**
 * Created by Red Hat Developer Studio
 *
 */
public class Welcome {
    String name;
    String inputName;

    public Welcome() {}

    public String check() {
    	inputName = name;
    	return "continue";
    }

    public void setName(String name) {
    	this.name = name;
    }

    public String getName() {
    	return name;
    }

    public void setInputName(String name) {
    	this.inputName = name;
    }

    public String getInputName() {
    	return inputName;
    }

    public String accept() {
    	name = inputName;
    	return "ok";
    }
}