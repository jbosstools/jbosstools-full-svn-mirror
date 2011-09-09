/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.ide.eclipse.as.openshift.internal.test.core;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.jboss.ide.eclipse.as.openshift.core.Domain;
import org.jboss.ide.eclipse.as.openshift.core.SSHKey;
import org.jboss.ide.eclipse.as.openshift.internal.core.OpenshiftService;
import org.junit.Before;
import org.junit.Test;

public class DomainIntegrationTest {

	private OpenshiftService openshiftService;

	private static final String USERNAME = "openshift.only.domain@gmail.com";
	private static final String PASSWORD = "1q2w3e";

	private static final String privateKey =
			"-----BEGIN RSA PRIVATE KEY-----\n" +
					"Proc-Type: 4,ENCRYPTED\n" +
					"DEK-Info: DES-EDE3-CBC,30E8B3A668E44D77\n" +
					"\n" +
					"wsXCU31SUTYhjPnOiW56UlZ7VXKlIjItLo5Wrk5LwCLv/OERK7UWJkrwM2bHPb6n\n" +
					"3zAaDDZfQe786URQjfmUYWBdVeI7DrRMwoLaaUcR1tuJtMu3Jv3CK72YMkOzGapZ\n" +
					"ZAAoTno/GRhTwptXG1KPSqKyzfqlxjbZry1HZmY+P6ikw9DWOPZC6rISIqQ3u9zm\n" +
					"iPvi/Oo7JWZtX1d1MYp3vVt2bo2duD4RSoXWWaW471WUOIQZUh0V4dxp+eAHZziu\n" +
					"osAfU4WoIrrSCSVl2uiKS2Zijn77PvcCXnm45eMQpww32AlslzIBNsMzUXhPtVAZ\n" +
					"a9uvfZxlOIRu4ObN7AB3WExucbBHCvTOgxpSs95br1QtfMVl62d9VkIAXg1x9gH8\n" +
					"kjcEP+0OS3EItYTFj1tCKC8GgBImj44AxbPSWu3SfTnYfAtFnO0pUqhPOBN63DCC\n" +
					"XxzMm13UeER7Z3s968Swa48r6LRAbHI8JD0Ld4E02fgBYM/N/aGtPppD0FoJJwo3\n" +
					"QVafS2PY+bALgy4qrI9daOo1mTS3gWDSAG0QbLoKd3hD8ZnLEk6rfR/0SE34Fc7j\n" +
					"mviCy78C16hkllhWz27ROl5pheHV0Xt6ZlUsNWVz7tg/AcIFB0geMuzuM0Kd7ufj\n" +
					"g5c8mhlI06n4vzo0uB6UXtwTBzNqyUl8yxA31S2VJfBZxkEwKc5cktNUiejQuWbU\n" +
					"iwapdiSR0gNGyYBNMYax9OOfYH+BBQeD5kboVU3yvT7UNcz0T9GZiEhfvcaYSP8C\n" +
					"ejQ1vuTNTKMrgyLpNi/4Sq8lm8OukRqQyE0EKYCwvkI=\n" +
					"-----END RSA PRIVATE KEY-----";

	private static final String publicKey =
			"ssh-rsa "
					+
					"AAAAB3NzaC1yc2EAAAADAQABAAAAgQC6BGRDydfGsQHhnZgo43dEfLzSJBke/hE8MLBBG1+5Z" +
					"wktsrE+f2VdVt0McRLVAO6rdJRyMUX0rTbm7SABRVSX+zeQjlfqbbUtYFc7TIfd4RQc3GaISG" +
					"1rS3C4svRSjdWaG36vDY2KxowdFvpKj8i8IYNPlLoRA/7EzzyneS6iyw" +
					"== created by org.jboss.ide.eclipse.as.openshift.core";

	@Before
	public void setUp() {
		this.openshiftService = new OpenshiftService(USERNAME, PASSWORD);
	}

	@Test
	public void canCreateDomain() throws Exception {
		File privateKeyFile = File.createTempFile(createRandomString(), null);
		writeTo(privateKey, privateKeyFile);

		File publicKeyFile = File.createTempFile(createRandomString(), null);
		writeTo(publicKey, publicKeyFile);

		SSHKey key = openshiftService.loadKey(privateKeyFile.getAbsolutePath(), publicKeyFile.getAbsolutePath());
		String domainName = createRandomString();
		Domain domain = openshiftService.createDomain(domainName, key);

		assertNotNull(domain);
	}

	private String createRandomString() {
		return String.valueOf(System.currentTimeMillis());
	}

	private void writeTo(String data, File file) throws IOException {
		StringReader reader = null;
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			reader = new StringReader(data);
			for (int character = -1; (character = reader.read()) != -1;) {
				writer.write(character);
			}
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}
}
