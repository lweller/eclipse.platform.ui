/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.commands.internal.HandlerServiceImpl;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.handlers.E4HandlerProxy;

public class MakeHandlersGo extends AbstractHandler {

	private IWorkbench workbench;
	private String commandId;

	public MakeHandlersGo(IWorkbench wb, String commandId) {
		workbench = wb;
		this.commandId = commandId;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		WorkbenchPlugin.log("Calling \"" + event.getCommand() //$NON-NLS-1$
				+ "\" command directly"); //$NON-NLS-1$
		Object obj = event.getApplicationContext();
		if (obj instanceof IEvaluationContext) {
			IHandlerService hs = (IHandlerService) workbench.getService(IHandlerService.class);
			if (hs != null) {
				ParameterizedCommand pcmd = generateCommand(event);
				if (pcmd != null) {
					Event e = null;
					if (event.getTrigger() instanceof Event) {
						e = (Event) event.getTrigger();
					}
					try {
						return hs.executeCommandInContext(pcmd, e, (IEvaluationContext) obj);
					} catch (NotDefinedException e1) {
						// Because of the expectations of 3.x, this should
						// go nowhere
					} catch (NotEnabledException e1) {
						// Because of the expectations of 3.x, this should
						// go nowhere
					} catch (NotHandledException e1) {
						// Because of the expectations of 3.x, this should
						// go nowhere
					}
				}
			}
		}
		return null;
	}

	private ParameterizedCommand generateCommand(ExecutionEvent event) {
		Command cmd = event.getCommand();
		if (event.getParameters().isEmpty()) {
			return new ParameterizedCommand(cmd, null);
		}
		ArrayList<Parameterization> parms = new ArrayList<Parameterization>();
		Iterator i = event.getParameters().entrySet().iterator();
		try {
			while (i.hasNext()) {
				Map.Entry entry = (Map.Entry) i.next();
				parms.add(new Parameterization(cmd.getParameter((String) entry.getKey()),
						(String) entry.getValue()));
			}
			return new ParameterizedCommand(cmd, parms.toArray(new Parameterization[parms.size()]));
		} catch (NotDefinedException e) {
			WorkbenchPlugin.log(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		EHandlerService hs = (EHandlerService) workbench.getService(EHandlerService.class);
		ECommandService cs = (ECommandService) workbench.getService(ECommandService.class);
		Command command = cs.getCommand(commandId);
		if (hs == null) {
			return false;
		}
		setBaseEnabled(hs.canExecute(new ParameterizedCommand(command, null)));
		return super.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#isHandled()
	 */
	@Override
	public boolean isHandled() {
		EHandlerService service = (EHandlerService) workbench.getService(EHandlerService.class);
		if (service == null) {
			return false;
		}
		IEclipseContext ctx = (IEclipseContext) workbench.getService(IEclipseContext.class);
		Object handler = HandlerServiceImpl.lookUpHandler(ctx, commandId);
		if (handler instanceof E4HandlerProxy) {
			IHandler h = ((E4HandlerProxy) handler).getHandler();
			return h == null ? false : h.isHandled();
		}
		return handler != null;
	}
}