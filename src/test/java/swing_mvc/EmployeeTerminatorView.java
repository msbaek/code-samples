package swing_mvc;

import java.util.Vector;

public interface EmployeeTerminatorView {
	void enableTerminate(boolean enable);
	void setEmployeeList(Vector employees);
	void clearSelection();
}
