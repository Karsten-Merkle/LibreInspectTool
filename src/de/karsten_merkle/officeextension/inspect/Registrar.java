/* Copyright 2012 Karsten Merkle
 * 
 *    This file is part of LibreOfficeInspect.
 *
 *    LibreOfficeInspect is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation version 2.0 of the License.
 *
 *    LibreOfficeInspect is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with LibreOfficeInspect.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.karsten_merkle.officeextension.inspect;

import com.sun.star.comp.loader.FactoryHelper;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.registry.XRegistryKey;

public class Registrar {
	private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Registrar.class.getName());
	
	public synchronized static XSingleComponentFactory __getComponentFactory(String implName) {
		LOGGER.finest("__getComponentFactory called: " + implName);
		if (implName.equals(JobExecutor.class.getName())) {
			return Factory.createComponentFactory(JobExecutor.class, new String[] { "com.sun.star.task.Job" });
		}
		if (implName.equals(TestProtocolHandler.class.getName())) {
			return Factory.createComponentFactory(TestProtocolHandler.class, TestProtocolHandler.getServiceNames());
		}
		LOGGER.finest("__getComponentFactory left");
		return null;
	}

	public static boolean __writeRegistryServiceInfo(XRegistryKey regKey) {
		LOGGER.finest("__writeRegistryServiceInfo called " + regKey.getKeyName());
		register(regKey, JobExecutor.class.getName(), new String[] { "com.sun.star.task.Job" });
		register(regKey, TestProtocolHandler.class.getName(), TestProtocolHandler.getServiceNames());
		LOGGER.finest("__writeRegistryServiceInfo finished ");
		return true;
	}

	private static void register(XRegistryKey regKey, String className, String[] services) {
		if (!FactoryHelper.writeRegistryServiceInfo(className, services, regKey)) {
			LOGGER.warning("failed to register " + className);
		} else {
			LOGGER.fine("registered " + className);
		}
	}

}