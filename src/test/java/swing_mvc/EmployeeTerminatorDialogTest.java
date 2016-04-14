package swing_mvc;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EmployeeTerminatorDialogTest implements EmployeeTerminatorPresenter {
	private EmployeeTerminatorDialog terminator;
	private JList list;
	private JButton button;
	private Container contentPane;
	private String selectedValue = null;
	private int selectionCount = 0;
	private int terminations = 0;

	@Before
	public void setUp() throws Exception {
		terminator = new EmployeeTerminatorDialog();
		terminator.initialize(this);
		putComponentsIntoMemberVariables();
	}

	private void putComponentsIntoMemberVariables() {
		contentPane = terminator.getContentPane();
		HashMap map = new HashMap();
		for (int i = 0; i < contentPane.getComponentCount(); i++) {
			Component c = contentPane.getComponent(i);
			map.put(c.getName(), c);
		}
		list = (JList) map.get(EmployeeTerminatorDialog.EMPLOYEE_LIST_NAME);
		button = (JButton) map.get(EmployeeTerminatorDialog.TERMINATE_BUTTON_NAME);
	}

	private void putThreeEmployeesIntoTerminator() {
		Vector v = new Vector();
		v.add("Bob");
		v.add("Bill");
		v.add("Boris");
		terminator.setEmployeeList(v);
	}

	@Test
	public void testCreate() throws Exception {
		assertNotNull(contentPane);
		assertEquals(2, contentPane.getComponentCount());
		assertNotNull(list);
		assertNotNull(button);
		assertEquals(false, button.isEnabled());
	}

	@Test
	public void testAddOneName() throws Exception {
		Vector v = new Vector();
		v.add("Bob");
		terminator.setEmployeeList(v);
		ListModel m = list.getModel();
		assertEquals(1, m.getSize());
		assertEquals("Bob", m.getElementAt(0));
	}

	@Test
	public void testAddManyNames() throws Exception {
		putThreeEmployeesIntoTerminator();
		ListModel m = list.getModel();
		assertEquals(3, m.getSize());
		assertEquals("Bob", m.getElementAt(0));
		assertEquals("Bill", m.getElementAt(1));
		assertEquals("Boris", m.getElementAt(2));
	}

	@Test
	public void testEnableTerminate() throws Exception {
		terminator.enableTerminate(true);
		assertEquals(true, button.isEnabled());
		terminator.enableTerminate(false);
		assertEquals(false, button.isEnabled());
	}

	@Test
	public void testClearSelection() throws Exception {
		putThreeEmployeesIntoTerminator();
		list.setSelectedIndex(1);
		assertNotNull(list.getSelectedValue());
		terminator.clearSelection();
		assertEquals(null, list.getSelectedValue());
	}

	@Test
	public void testSelectionChangedCallback() throws Exception {
		putThreeEmployeesIntoTerminator();
		list.setSelectedIndex(1);
		assertEquals("Bill", selectedValue);
		assertEquals(1, selectionCount);
		list.setSelectedIndex(2);
		assertEquals("Boris", selectedValue);
		assertEquals(2, selectionCount);
	}

	@Test
	public void testTerminateButtonCallback() throws Exception {
		button.doClick();
		assertEquals(1, terminations);
	}

	// implement EmployeeTerminatorPresenter
	@Override
	public void selectionChanged(String employee) {
		selectedValue = employee;
		selectionCount++;
	}

	@Override
	public void terminate() {
		terminations++;
	}
}
