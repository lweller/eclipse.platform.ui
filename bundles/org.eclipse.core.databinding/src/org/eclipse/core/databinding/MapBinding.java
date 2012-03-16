/*
 * MapBinding.java
 *
 * Version $Revision$, $Date$
 *
 * Project: Unified Network Objects UNO
 *
 * (c) 2011 by SBB
 */
/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lucien Weller
 ******************************************************************************/
package org.eclipse.core.databinding;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.ObservableTracker;
import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.map.MapDiff;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.internal.databinding.BindingStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

/**
 */
public class MapBinding extends Binding {
	private UpdateMapStrategy targetToModel;
	private UpdateMapStrategy modelToTarget;
	private IObservableValue validationStatusObservable;
	private boolean updatingTarget;
	private boolean updatingModel;

	private IMapChangeListener targetChangeListener = new IMapChangeListener() {
		public void handleMapChange(MapChangeEvent event) {
			if (!updatingTarget) {
				doUpdate((IObservableMap) getTarget(),
						(IObservableMap) getModel(), event.diff, targetToModel,
						false, false);
			}
		}
	};

	private IMapChangeListener modelChangeListener = new IMapChangeListener() {
		public void handleMapChange(MapChangeEvent event) {
			if (!updatingModel) {
				doUpdate((IObservableMap) getModel(),
						(IObservableMap) getTarget(), event.diff,
						modelToTarget, false, false);
			}
		}
	};

	/**
	 * @param target
	 * @param model
	 * @param targetToModelStrategy
	 * @param modelToTargetStrategy
	 */
	public MapBinding(IObservableMap target, IObservableMap model,
			UpdateMapStrategy targetToModelStrategy,
			UpdateMapStrategy modelToTargetStrategy) {
		super(target, model);
		this.targetToModel = targetToModelStrategy;
		this.modelToTarget = modelToTargetStrategy;

		if ((targetToModel.getUpdatePolicy() & UpdateMapStrategy.POLICY_UPDATE) != 0) {
			target.addMapChangeListener(targetChangeListener);
		} else {
			targetChangeListener = null;
		}

		if ((modelToTarget.getUpdatePolicy() & UpdateMapStrategy.POLICY_UPDATE) != 0) {
			model.addMapChangeListener(modelChangeListener);
		} else {
			modelChangeListener = null;
		}
	}

	public IObservableValue getValidationStatus() {
		return validationStatusObservable;
	}

	protected void preInit() {
		ObservableTracker.runAndIgnore(new Runnable() {
			public void run() {
				validationStatusObservable = new WritableValue(context
						.getValidationRealm(), Status.OK_STATUS, IStatus.class);
			}
		});
	}

	protected void postInit() {
		if (modelToTarget.getUpdatePolicy() == UpdateMapStrategy.POLICY_UPDATE) {
			updateModelToTarget();
		}

		if (targetToModel.getUpdatePolicy() == UpdateMapStrategy.POLICY_UPDATE) {
			validateTargetToModel();
		}
	}

	public void updateModelToTarget() {
		final IObservableMap modelMap = (IObservableMap) getModel();
		modelMap.getRealm().exec(new Runnable() {
			public void run() {
				MapDiff diff = Diffs.computeMapDiff(Collections.EMPTY_MAP,
						modelMap);
				doUpdate(modelMap, (IObservableMap) getTarget(), diff,
						modelToTarget, true, true);
			}
		});
	}

	public void updateTargetToModel() {
		final IObservableMap targetMap = (IObservableMap) getTarget();
		targetMap.getRealm().exec(new Runnable() {
			public void run() {
				MapDiff diff = Diffs.computeMapDiff(Collections.EMPTY_MAP,
						targetMap);
				doUpdate(targetMap, (IObservableMap) getModel(), diff,
						targetToModel, true, true);
			}
		});
	}

	public void validateModelToTarget() {
		// nothing for now
	}

	public void validateTargetToModel() {
		// nothing for now
	}

	/*
	 * This method may be moved to UpdateMapStrategy in the future if clients
	 * need more control over how the two sets are kept in sync.
	 */
	private void doUpdate(final IObservableMap source,
			final IObservableMap destination, final MapDiff diff,
			final UpdateMapStrategy updateMapStrategy, final boolean explicit,
			final boolean clearDestination) {
		final int policy = updateMapStrategy.getUpdatePolicy();

		if (policy == UpdateMapStrategy.POLICY_NEVER) {
			return;
		}

		if (policy == UpdateMapStrategy.POLICY_ON_REQUEST && !explicit) {
			return;
		}

		destination.getRealm().exec(new Runnable() {
			public void run() {
				if (destination == getTarget()) {
					updatingTarget = true;
				} else {
					updatingModel = true;
				}

				MultiStatus multiStatus = BindingStatus.ok();

				try {
					if (clearDestination) {
						destination.clear();
					}

					for (Iterator iterator = diff.getRemovedKeys().iterator(); iterator
							.hasNext();) {
						IStatus setterStatus = updateMapStrategy.doRemove(
								destination, iterator.next());

						mergeStatus(multiStatus, setterStatus);
						// TODO - at this point, the two sets
						// will be out of sync if an error
						// occurred...
					}

					for (Iterator iterator = diff.getAddedKeys().iterator(); iterator
							.hasNext();) {
						Object key = iterator.next();
						IStatus setterStatus = updateMapStrategy.doPut(
								destination, key, updateMapStrategy
										.convert(diff.getNewValue(key)));

						mergeStatus(multiStatus, setterStatus);
						// TODO - at this point, the two sets
						// will be out of sync if an error
						// occurred...
					}

					for (Iterator iterator = diff.getChangedKeys().iterator(); iterator
							.hasNext();) {
						Object key = iterator.next();
						IStatus setterStatus = updateMapStrategy.doPut(
								destination, key, updateMapStrategy
										.convert(diff.getNewValue(key)));

						mergeStatus(multiStatus, setterStatus);
						// TODO - at this point, the two sets
						// will be out of sync if an error
						// occurred...
					}
				} finally {
					setValidationStatus(multiStatus);

					if (destination == getTarget()) {
						updatingTarget = false;
					} else {
						updatingModel = false;
					}
				}
			}
		});
	}

	private void setValidationStatus(final IStatus status) {
		validationStatusObservable.getRealm().exec(new Runnable() {
			public void run() {
				validationStatusObservable.setValue(status);
			}
		});
	}

	/**
	 * Merges the provided <code>newStatus</code> into the
	 * <code>multiStatus</code>.
	 * 
	 * @param multiStatus
	 * @param newStatus
	 */
	/* package */void mergeStatus(MultiStatus multiStatus, IStatus newStatus) {
		if (!newStatus.isOK()) {
			multiStatus.add(newStatus);
		}
	}

	public void dispose() {
		if (targetChangeListener != null) {
			((IObservableMap) getTarget())
					.removeMapChangeListener(targetChangeListener);
			targetChangeListener = null;
		}

		if (modelChangeListener != null) {
			((IObservableMap) getModel())
					.removeMapChangeListener(modelChangeListener);
			modelChangeListener = null;
		}

		super.dispose();
	}
}
