package org.eclipse.ui.internal.dialogs;

import java.util.Locale;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class FontPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	/*
	 * @see FieldEditorPreferencePage#createFieldEditors
	 */
	public void createFieldEditors() {

		Composite editorParent = getFieldEditorParent();

		createFieldEditor(
			JFaceResources.getBannerFont(),
			JFaceResources.BANNER_FONT,
			WorkbenchMessages.getString("FontsPreference.BannerFont"),
			editorParent);
		createFieldEditor(
			JFaceResources.getDialogFont(),
			JFaceResources.DIALOG_FONT,
			WorkbenchMessages.getString("FontsPreference.DialogFont"),
			editorParent);
		createFieldEditor(
			JFaceResources.getHeaderFont(),
			JFaceResources.HEADER_FONT,
			WorkbenchMessages.getString("FontsPreference.HeaderFont"),
			editorParent);
		createFieldEditor(
			JFaceResources.getTextFont(),
			JFaceResources.TEXT_FONT,
			WorkbenchMessages.getString("FontsPreference.TextFont"),
			editorParent);
	}

	/**
	 * Create the preference page.
	 */
	public FontPreferencePage() {
		super(GRID);

		Plugin plugin = Platform.getPlugin(PlatformUI.PLUGIN_ID);
		if (plugin instanceof AbstractUIPlugin) {
			AbstractUIPlugin uiPlugin = (AbstractUIPlugin) plugin;
			setPreferenceStore(uiPlugin.getPreferenceStore());
		}
	}

	/**
	 * Create a field editor for the setting. Also initialize 
	 * the setting to the current font
	 */

	private void createFieldEditor(
		Font currentSetting,
		String preferenceName,
		String title,
		Composite editorParent) {

		addField(
			new FontFieldEditor(
				preferenceName, 
				title, 
				editorParent));
	}

	/*
	 * @see IWorkbenchPreferencePage#init
	 */
	public void init(IWorkbench workbench) {
	}


}