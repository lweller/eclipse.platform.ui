/*******************************************************************************
 * Copyright (c) 2008 Matthew Hall and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 194734)
 ******************************************************************************/

package org.eclipse.jface.internal.databinding.viewers;

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.value.IValuePropertyChangeListener;
import org.eclipse.jface.viewers.Viewer;

/**
 * @since 3.3
 * 
 */
public class ViewerInputProperty extends ViewerValueProperty {
	protected Object getValueType() {
		return null;
	}

	protected Object doGetValue(Object source) {
		return ((Viewer) source).getInput();
	}

	protected void doSetValue(Object source, Object value) {
		((Viewer) source).setInput(value);
	}

	protected INativePropertyListener adaptListener(
			IValuePropertyChangeListener listener) {
		return null;
	}

	protected void doAddListener(Object source, INativePropertyListener listener) {
	}

	protected void doRemoveListener(Object source,
			INativePropertyListener listener) {
	}

	public String toString() {
		return "Viewer.input"; //$NON-NLS-1$
	}
}
