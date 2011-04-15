/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.tests.jbt.resolution;

import java.util.List;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;

import org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.*;

public class Zoo {

	   @Produces @Typed(List.class)
	   private ArrayList<Cat<European>> catsProducer = new ArrayList<Cat<European>>();
	   
	   @Produces @Typed(List.class)
	   public ArrayList<Lion> getLions()  {
	      return new ArrayList<Lion>();
	   }

	   @Inject
	   List<Cat<European>> cats;
	   
	   @Inject
	   List<Lion> lions;
	
}
