package swing_mvc;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class EmployeeTerminatorDialog implements EmployeeTerminatorView {
	private JFrame frame;
	private JList listBox;
	private JButton terminateButton;
	private EmployeeTerminatorPresenter presenter;
	private Vector employees;
	public static final String EMPLOYEE_LIST_NAME = "Employee List";
	public static final String TERMINATE_BUTTON_NAME = "Terminate";

	public void initialize(EmployeeTerminatorPresenter presenter) {
		this.presenter = presenter;
		initializeEmployeeListBox();
		initializeTerminateButton();
		initializeContentPane();
	}

	private void initializeEmployeeListBox() {
		listBox = new JList();
		listBox.setName(EMPLOYEE_LIST_NAME);
		listBox.addListSelectionListener(
				e -> {
					if (!e.getValueIsAdjusting())
						presenter.selectionChanged((String) listBox.getSelectedValue());
				}
		);
	}

	private void initializeTerminateButton() {
		terminateButton = new JButton(TERMINATE_BUTTON_NAME);
		terminateButton.disable();
		terminateButton.setName(TERMINATE_BUTTON_NAME);
		terminateButton.addActionListener(e -> presenter.terminate());
	}

	private void initializeContentPane() {
		frame = new JFrame("Employee List");
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(listBox);
		frame.getContentPane().add(terminateButton);
		frame.getContentPane().setSize(300, 600);
		frame.pack();
	}

	public Container getContentPane() {
		return frame.getContentPane();
	}

	public JFrame getFrame() {
		return frame;
	}

	// functions for EmployeeTerminatorView interface
	@Override
	public void enableTerminate(boolean enable) {
		terminateButton.setEnabled(enable);
	}

	@Override
	public void setEmployeeList(Vector employees) {
		this.employees = employees;
		listBox.setListData(employees);
		frame.pack();
	}

	@Override
	public void clearSelection() {
		listBox.clearSelection();
	}
}
