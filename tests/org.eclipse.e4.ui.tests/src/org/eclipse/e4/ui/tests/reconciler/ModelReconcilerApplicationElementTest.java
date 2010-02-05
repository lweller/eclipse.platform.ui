/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.tests.reconciler;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.MCommand;
import org.eclipse.e4.workbench.modeling.ModelDelta;
import org.eclipse.e4.workbench.modeling.ModelReconciler;

public abstract class ModelReconcilerApplicationElementTest extends
		ModelReconcilerTest {

	private void testApplicationElement_Style(String[] originalTags,
			String[] userTags, String[] newTags, String[] mergedTags) {
		MApplication application = createApplication();
		application.getTags().addAll(Arrays.asList(originalTags));

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		application.getTags().clear();
		application.getTags().addAll(Arrays.asList(userTags));

		Object state = reconciler.serialize();

		application = createApplication();
		application.getTags().clear();
		List<String> newTagsList = Arrays.asList(newTags);
		application.getTags().addAll(newTagsList);

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(newTagsList, application.getTags());

		applyAll(deltas);

		List<String> mergedTagsList = Arrays.asList(mergedTags);
		assertEquals(mergedTagsList.size(), application.getTags().size());
		assertTrue(mergedTagsList.containsAll(application.getTags()));
		assertTrue(application.getTags().containsAll(mergedTagsList));
	}

	public void testApplicationElement_Style() {
		testApplicationElement_Style(new String[0], new String[0],
				new String[0], new String[0]);
	}

	public void testApplicationElement_Style2() {
		testApplicationElement_Style(new String[] { "cvs" }, new String[0],
				new String[] { "cvs" }, new String[0]);
	}

	public void testApplicationElement_Style3() {
		testApplicationElement_Style(new String[] { "cvs" },
				new String[] { "cvs" }, new String[] { "cvs" },
				new String[] { "cvs" });
	}

	public void testApplicationElement_Style4() {
		testApplicationElement_Style(new String[] { "cvs" }, new String[] {
				"cvs", "scm" }, new String[] { "cvs" }, new String[] { "cvs",
				"scm" });
	}

	public void testApplicationElement_Style5() {
		testApplicationElement_Style(new String[0], new String[] { "cvs" },
				new String[] { "scm" }, new String[] { "scm", "cvs" });
	}

	private void testApplicationElement_Id_New(boolean createIdFirst) {
		MApplication application = createApplication();

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		MCommand command = MApplicationFactory.eINSTANCE.createCommand();
		if (createIdFirst) {
			command.setId("commandId");
			application.getCommands().add(command);
		} else {
			application.getCommands().add(command);
			command.setId("commandId");
		}

		Object state = reconciler.serialize();

		application = createApplication();

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(0, application.getCommands().size());

		applyAll(deltas);

		assertEquals(1, application.getCommands().size());

		command = application.getCommands().get(0);
		assertEquals("commandId", command.getId());
	}

	public void testApplicationElement_Id_New_True() {
		testApplicationElement_Id_New(true);
	}

	public void testApplicationElement_Id_New_False() {
		testApplicationElement_Id_New(false);
	}

	private void testApplicationElement_Id(String before, String after) {
		MApplication application = createApplication();
		application.setId(before);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		application.setId(after);

		Object state = reconciler.serialize();

		application = createApplication();

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(before, application.getId());

		applyAll(deltas);

		assertEquals(after, application.getId());
	}

	public void testApplicationElement_Id_NullNull() {
		testApplicationElement_Id(null, null);
	}

	public void testApplicationElement_Id_NullEmpty() {
		testApplicationElement_Id(null, "");
	}

	public void testApplicationElement_Id_NullString() {
		testApplicationElement_Id(null, "id");
	}

	public void testApplicationElement_Id_EmptyNull() {
		testApplicationElement_Id("", null);
	}

	public void testApplicationElement_Id_EmptyEmpty() {
		testApplicationElement_Id("", "");
	}

	public void testApplicationElement_Id_EmptyString() {
		testApplicationElement_Id("", "id");
	}

	public void testApplicationElement_Id_StringNull() {
		testApplicationElement_Id("id", null);
	}

	public void testApplicationElement_Id_StringEmpty() {
		testApplicationElement_Id("id", "");
	}

	public void testApplicationElement_Id_StringStringUnchanged() {
		testApplicationElement_Id("id", "id");
	}

	public void testApplicationElement_Id_StringStringChanged() {
		testApplicationElement_Id("id", "id3");
	}
}