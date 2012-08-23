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


import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XIntrospection;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XDispatch;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStatusListener;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.URL;

public class IntrospectDispatch implements XDispatch {
	private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(IntrospectDispatch.class.getName());
	private XComponentContext context;
	XDesktop desktop;

	public IntrospectDispatch(XComponentContext compContext) {
		context = compContext;
		try {
			Object desktopObj = context.getServiceManager().createInstanceWithContext("com.sun.star.frame.Desktop", context);
			desktop = UnoRuntime.queryInterface(XDesktop.class, desktopObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addStatusListener(XStatusListener arg0, URL arg1) {
		// NO CODE
	}

	@Override
	public void dispatch(URL url, PropertyValue[] values) {
		LOGGER.finest("TestDispatch dispatch called " + url.Complete);
		System.out.println("TestDispatch dispatch called " + url.Complete);
		Object probe = null;
		if (TestProtocolHandler.INSPECTSEL.equals(url.Complete)) {
			XComponent component = desktop.getCurrentComponent();
			XModel model = UnoRuntime.queryInterface(XModel.class, component);
			if (model != null) {
				probe = model.getCurrentSelection();
			}
		} else if (TestProtocolHandler.INSPECTDOC.equals(url.Complete)) {
			probe = desktop.getCurrentComponent();
		} else if (TestProtocolHandler.INSPECTDES.equals(url.Complete)) {
			probe = desktop;
		} else return;
		if (probe != null) {
			inspect(probe);
		}
	}

	@Override
	public void removeStatusListener(XStatusListener arg0, URL arg1) {
		// NO CODE
	}

	private void inspect(Object obj) {
		if (obj.getClass().isArray()) {
			for (Object object : (Object[]) obj) {
				inspect(object);
			}
			return;
		}
		try {
			Object oMRI = context.getServiceManager().createInstanceWithContext("mytools.Mri", context);
			XIntrospection xIntrospection = (XIntrospection) UnoRuntime.queryInterface(XIntrospection.class, oMRI);
			xIntrospection.inspect(obj);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
