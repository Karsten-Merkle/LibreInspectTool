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



import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XIndexContainer;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.ui.ActionTriggerSeparatorType;
import com.sun.star.ui.ContextMenuExecuteEvent;
import com.sun.star.ui.ContextMenuInterceptorAction;
import com.sun.star.ui.XContextMenuInterceptor;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;

public class IntrospectMenuInterceptor implements XContextMenuInterceptor, XInterface {
	private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(IntrospectMenuInterceptor.class
			.getName());

	@Override
	public ContextMenuInterceptorAction notifyContextMenuExecute(ContextMenuExecuteEvent contextEvent) {
		try {
			LOGGER.finest("start");
			XIndexContainer xContextMenu = contextEvent.ActionTriggerContainer;
			XMultiServiceFactory xMSF = UnoRuntime.queryInterface(XMultiServiceFactory.class, xContextMenu);
			if (xMSF == null) {
				LOGGER.finest("xMenuElementFactory is null");
				return ContextMenuInterceptorAction.IGNORED;
			}
			try {
				XPropertySet separator =
						UnoRuntime
								.queryInterface(XPropertySet.class, xMSF.createInstance("com.sun.star.ui.ActionTriggerSeparator"));
				separator.setPropertyValue("SeparatorType", new Short(ActionTriggerSeparatorType.LINE));
				xContextMenu.insertByIndex(0, (Object) separator);
//				xContextMenu.insertByIndex(0, getMenuEntry(new String("Inspect Selection"), "service:mytools.Mri?selection", xMSF));
//				xContextMenu.insertByIndex(0, getMenuEntry(new String("Inspect Document"), "service:mytools.Mri?current", xMSF));
//				xContextMenu.insertByIndex(0, getMenuEntry(new String("Inspect Desktop"), "service:mytools.Mri", xMSF));				
				xContextMenu.insertByIndex(0, getMenuEntry(new String("Inspect Selection"), TestProtocolHandler.INSPECTSEL, xMSF));
				xContextMenu.insertByIndex(0, getMenuEntry(new String("Inspect Document"), TestProtocolHandler.INSPECTDOC, xMSF));
				xContextMenu.insertByIndex(0, getMenuEntry(new String("Inspect Desktop"), TestProtocolHandler.INSPECTDES, xMSF));
				xContextMenu.insertByIndex(0, (Object) separator);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (WrappedTargetException e) {
				e.printStackTrace();
			} catch (UnknownPropertyException e) {
				e.printStackTrace();
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			} catch (com.sun.star.uno.Exception e) {
				e.printStackTrace();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ContextMenuInterceptorAction.CONTINUE_MODIFIED;
	}

	private XPropertySet getMenuEntry(String text, String url, XMultiServiceFactory serviceFactory) throws Exception {
		LOGGER.finest("make: " + text + "\t" + url);
		XPropertySet menuEntry =
				(XPropertySet) UnoRuntime.queryInterface(XPropertySet.class,
						serviceFactory.createInstance("com.sun.star.ui.ActionTrigger"));
		menuEntry.setPropertyValue("Text", text);
		menuEntry.setPropertyValue("CommandURL", url);
		return menuEntry;
	}

}
