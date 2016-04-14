package swing_mvc;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class EmployeeTerminatorRunner {
	static Vector employees = new Vector();
	static EmployeeTerminatorDialog dialog;

	public static void main(String[] args) {
		initializeEmployeeVector();
		initializeDialog();
		runDialog();
	}

	private static void initializeEmployeeVector() {
		employees.add("Bob");
		employees.add("Bill");
		employees.add("Robert");
	}

	private static void initializeDialog() {
		EmployeeTerminatorPresenterImpl model = new EmployeeTerminatorPresenterImpl();
		dialog = new EmployeeTerminatorDialog();
		dialog.initialize(model);
		model.initialize(employees, dialog);
	}

	private static void runDialog() {
		dialog.getFrame().addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				for (int i = 0; i < employees.size(); i++) {
					String s = (String) employees.elementAt(i);
					System.out.println(s);
				}
				System.exit(0);
			}
		});
		dialog.getFrame().setVisible(true);
	}
}
