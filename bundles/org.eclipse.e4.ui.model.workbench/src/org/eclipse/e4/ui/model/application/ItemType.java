/**
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      IBM Corporation - initial API and implementation
 */
package org.eclipse.e4.ui.model.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Item Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.ui.model.application.MApplicationPackage#getItemType()
 * @model
 * @generated
 */
public enum ItemType implements Enumerator {
	/**
	 * The '<em><b>Push</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PUSH_VALUE
	 * @generated
	 * @ordered
	 */
	PUSH(0, "Push", "Push"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Check</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHECK_VALUE
	 * @generated
	 * @ordered
	 */
	CHECK(1, "Check", "Check"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Radio</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RADIO_VALUE
	 * @generated
	 * @ordered
	 */
	RADIO(2, "Radio", "Radio"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Separator</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SEPARATOR_VALUE
	 * @generated
	 * @ordered
	 */
	SEPARATOR(3, "Separator", "Separator"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Push</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Push</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PUSH
	 * @model name="Push"
	 * @generated
	 * @ordered
	 */
	public static final int PUSH_VALUE = 0;

	/**
	 * The '<em><b>Check</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Check</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHECK
	 * @model name="Check"
	 * @generated
	 * @ordered
	 */
	public static final int CHECK_VALUE = 1;

	/**
	 * The '<em><b>Radio</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Radio</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #RADIO
	 * @model name="Radio"
	 * @generated
	 * @ordered
	 */
	public static final int RADIO_VALUE = 2;

	/**
	 * The '<em><b>Separator</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Separator</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SEPARATOR
	 * @model name="Separator"
	 * @generated
	 * @ordered
	 */
	public static final int SEPARATOR_VALUE = 3;

	/**
	 * An array of all the '<em><b>Item Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ItemType[] VALUES_ARRAY =
		new ItemType[] {
			PUSH,
			CHECK,
			RADIO,
			SEPARATOR,
		};

	/**
	 * A public read-only list of all the '<em><b>Item Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ItemType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Item Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ItemType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ItemType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Item Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ItemType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ItemType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Item Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ItemType get(int value) {
		switch (value) {
			case PUSH_VALUE: return PUSH;
			case CHECK_VALUE: return CHECK;
			case RADIO_VALUE: return RADIO;
			case SEPARATOR_VALUE: return SEPARATOR;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private ItemType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //ItemType