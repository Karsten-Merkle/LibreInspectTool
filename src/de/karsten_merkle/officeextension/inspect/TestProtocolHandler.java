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

import com.sun.star.frame.DispatchDescriptor;
import com.sun.star.frame.XDispatch;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.frame.XDispatchProviderInterceptor;
import com.sun.star.lang.XInitialization;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.lang.XServiceName;
import com.sun.star.uno.Exception;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.util.URL;

public class TestProtocolHandler implements XDispatchProviderInterceptor, XServiceInfo, XServiceName, XInterface, XInitialization {
	private static java.util.logging.Logger LOGGER = java.util.logging.Logger
			.getLogger(TestProtocolHandler.class.getName());
	protected static final String __serviceName = "com.sun.star.frame.ProtocolHandler";
	protected static final String[] __serviceNames = { __serviceName };

	public static final String PROTOCOLL = "de.karsten_merkle.officeextension.inspect:";
	public static final String INSPECTDES = PROTOCOLL+"Desktop";
	public static final String INSPECTSEL = PROTOCOLL+"Selection";
	public static final String INSPECTDOC = PROTOCOLL+"Component";

	private XDispatchProvider master;
	private XDispatchProvider slave;

	private XComponentContext context;

	public TestProtocolHandler(XComponentContext compContext) {
		context = compContext;
	}

	@Override
	public XDispatch queryDispatch(URL url, String targetFrameName, int searchFlags) {
		LOGGER.finest("ProtocolHandler queryDispatch called: " + url.Complete);
		if (url.Complete.startsWith(PROTOCOLL)) {
			IntrospectDispatch dispatch = new IntrospectDispatch(context);
			return dispatch;
		}
		if (this.slave != null) {
			return this.slave.queryDispatch(url, targetFrameName, searchFlags);
		}
		return null;
	}

	@Override
	public XDispatch[] queryDispatches(DispatchDescriptor[] arg0) {
		LOGGER.finest("ProtocolHandler queryDispatches called: ");
		if (this.slave != null) {
			return this.slave.queryDispatches(arg0);
		}
		return null;
	}

	@Override
	public XDispatchProvider getMasterDispatchProvider() {
		return master;
	}

	@Override
	public XDispatchProvider getSlaveDispatchProvider() {
		return slave;
	}

	@Override
	public void setMasterDispatchProvider(XDispatchProvider arg0) {
		master = arg0;
	}

	@Override
	public void setSlaveDispatchProvider(XDispatchProvider arg0) {
		slave = arg0;
	}

	@Override
	public void initialize(Object[] arg0) throws Exception {
	}

	@Override
	public String[] getSupportedServiceNames() {
		LOGGER.finest("start");
		return __serviceNames;
	}

	@Override
	public boolean supportsService(String serviceName) {
		LOGGER.finest("start");
		for (String sName : __serviceNames) {
			if (sName.equals(serviceName))
				return true;
		}
		return false;
	}

	@Override
	public String getServiceName() {
		LOGGER.finest("start");
		return __serviceName;
	}

	public static String[] getServiceNames() {
		LOGGER.finest("start");
		return __serviceNames;
	}

	@Override
	public String getImplementationName() {
		return this.getClass().getName();
	}
}
