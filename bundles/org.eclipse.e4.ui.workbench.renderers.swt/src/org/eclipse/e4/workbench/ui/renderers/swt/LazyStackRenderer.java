/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.workbench.ui.renderers.swt;

import org.eclipse.e4.ui.model.application.MApplicationPackage;
import org.eclipse.e4.ui.model.application.MElementContainer;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MPartStack;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.widgets.CTabFolder;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

/**
 * This class encapsulates the functionality necessary to manage stacks of parts
 * in a 'lazy loading' manner. For these stacks only the currently 'active'
 * child <b>most</b> be rendered so in this class we over ride that default
 * behavior for processing the stack's contents to prevent all of the contents
 * from being rendered, calling 'childAdded' instead. This not only saves time
 * and SWT resources but is necessary in an IDE world where we must not
 * arbitrarily cause plug-in loading.
 * 
 */
public abstract class LazyStackRenderer extends SWTPartRenderer {
	public LazyStackRenderer() {
		super();
	}

	public void postProcess(MUIElement element) {
		if (!(element instanceof MPartStack))
			return;

		MPartStack stack = (MPartStack) element;
		MPart selPart = stack.getActiveChild();

		// If there's no 'active' part defined then pick the first
		if (selPart == null && stack.getChildren().size() > 0) {
			// NOTE: no need to render first because the listener for
			// the active child changing will do it
			stack.setActiveChild(stack.getChildren().get(0));
		} else if (selPart != null && selPart.getWidget() == null) {
			renderer.createGui(selPart);
		}
	}

	@Override
	public void processContents(MElementContainer<MUIElement> me) {
		if (!(((MUIElement) me) instanceof MPartStack))
			return;

		MPartStack stack = (MPartStack) ((MUIElement) me);
		Widget parentWidget = getParentWidget(me);
		if (parentWidget == null)
			return;

		// Lazy Loading: here we only process the contents through childAdded,
		// we specifically do not render them
		for (MPart part : stack.getChildren()) {
			if (part.isVisible())
				showChild(stack, part);
		}
	}

	/**
	 * This method is necessary to allow the parent container to show affordance
	 * (i.e. tabs) for child elements -without- creating the actual part
	 * 
	 * @param stack
	 *            The parent model element
	 * @param childME
	 *            The child to show the affordance for
	 */
	protected void showChild(MPartStack stack, MPart childME) {
	}

	@Override
	public void hookControllerLogic(final MUIElement me) {
		super.hookControllerLogic(me);

		if (!(me instanceof MPartStack))
			return;

		final MPartStack sm = (MPartStack) me;

		// Detect activation...picks up cases where the user clicks on the
		// (already active) part
		if (sm.getWidget() instanceof Control) {
			Control ctrl = (Control) sm.getWidget();
			ctrl.addListener(SWT.Activate, new Listener() {
				public void handleEvent(Event event) {
					CTabFolder ctf = (CTabFolder) event.widget;
					MPartStack stack = (MPartStack) ctf.getData(OWNING_ME);
					MPart selPart = stack.getActiveChild();
					if (selPart != null)
						activate(selPart);
				}
			});
		}

		// Listen for changes to the 'activeChild'. If necessary render the
		// contents of the newly activated child before making it active
		((EObject) me).eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (MApplicationPackage.Literals.ELEMENT_CONTAINER__ACTIVE_CHILD
						.equals(msg.getFeature())) {
					MPartStack stack = (MPartStack) msg.getNotifier();
					MPart selPart = stack.getActiveChild();
					if (selPart != null && selPart.getWidget() == null)
						renderer.createGui(selPart);
					// activate(selPart);
				}
			}
		});
	}
}