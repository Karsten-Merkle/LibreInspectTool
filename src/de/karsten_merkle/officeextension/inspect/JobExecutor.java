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

import com.sun.star.beans.NamedValue;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDispatchProviderInterception;
import com.sun.star.frame.XModel;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.task.XJob;
import com.sun.star.ui.XContextMenuInterception;
import com.sun.star.ui.XContextMenuInterceptor;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Exception;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.view.XSelectionSupplier;

public class JobExecutor extends WeakBase implements XJob {
	private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(JobExecutor.class.getName());
	private XComponentContext compContext = null;

	public JobExecutor(XComponentContext context) {
		compContext = context;
		LOGGER.finest("Log: JobExecutor initialised.");
	}

	@Override
	public Object execute(NamedValue[] args) throws IllegalArgumentException, Exception {
		LOGGER.finest("JobExecutor execute");
		for (NamedValue arg : args) {
			if (arg.Name.equals("Environment")) {
				NamedValue[] values =
						(NamedValue[]) AnyConverter.toObject(new Type("[]com.sun.star.beans.NamedValue"), arg.Value);
				this.lookInto(values);
			} else {
				LOGGER.finest("not handled NamedValue.Name: " + arg.Name);
			}
		}
		LOGGER.finest("JobExecutor execute finished");
		return null;
	}

	private void lookInto(NamedValue[] values) {
		try {
			String envType = null;
			XModel xModel = null;
			for (NamedValue value : values) {
				if (value.Name.equals("Model")) {
					xModel = UnoRuntime.queryInterface(XModel.class, value.Value);
					LOGGER.finest("found XModel");
				} else if (value.Name.equals("EnvType")) {
					envType = (String) value.Value;
					LOGGER.finest("found EnvType: " + envType);
				} else {
					LOGGER.finest("not handled SubNamedValue.Name: " + value.Name);
				}
			}
			if (envType.equals("DOCUMENTEVENT")) {
				XController xController = xModel.getCurrentController();
				addProtocolHandler(xController);
				addMenuInterceptor(xController);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void addProtocolHandler(XController xController) {
		XDispatchProviderInterception pi =
				UnoRuntime.queryInterface(XDispatchProviderInterception.class, xController.getFrame());
		XSelectionSupplier selSupl = UnoRuntime.queryInterface(XSelectionSupplier.class, xController);
		if (pi != null && selSupl != null) {
			pi.registerDispatchProviderInterceptor(new TestProtocolHandler(compContext));
			LOGGER.finest("inserted TestProtocolHandler");
		}
	}
		
	private void addMenuInterceptor(XController xController) {
		XContextMenuInterception menInt = UnoRuntime.queryInterface(XContextMenuInterception.class, xController);
		if (menInt != null) {
			IntrospectMenuInterceptor menuInterceptor = new IntrospectMenuInterceptor();
			XContextMenuInterceptor xMenuInterceptor =
					UnoRuntime.queryInterface(XContextMenuInterceptor.class, menuInterceptor);
			menInt.registerContextMenuInterceptor(xMenuInterceptor);
			LOGGER.finest("inserted TestMenuInterceptor");
		}
	}

}
