package sms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Manages the GUI for the Student Management System, allowing users to add, update, and delete student records.
 */
public class ManagementView {

	// Main frame for the management window
	static JFrame managementFrame;

	// Table to display student records
	static JTable table;

	// Input fields for student details
	static JTextField nameField;
	static JTextField surnameField;
	static JTextField ageField;
	static JTextField startedDateField;

	// Dropdowns for gender and course selection
	static JComboBox<String> genderSelectionBox;
	static JComboBox<String> courseSelectionBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			Translator.getMessagesFromXML(); // Load language translations
			try {
				ManagementView window = new ManagementView();
				window.managementFrame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Initialize the ManagementView.
	 */
	public ManagementView() {
		initialize();
		table.clearSelection(); // Clear table selection to avoid update issues
		managementFrame.setVisible(true);
		DBHandler.updateStudents(); // Populate table with student data
	}

	/**
	 * Updates the list of courses in the course selection dropdown.
	 */
	private void updateCourses() {
		DefaultComboBoxModel<String> courses = new DefaultComboBoxModel<>(DBHandler.getCourses());
		courseSelectionBox.setModel(courses);
	}

	/**
	 * Initializes the GUI components of the frame.
	 */
	private void initialize() {
		// Set up the main frame
		managementFrame = new JFrame();
		managementFrame.setBounds(100, 100, 860, 540);
		managementFrame.setResizable(false);
		managementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		managementFrame.setTitle(Translator.getValue("sms"));
		managementFrame.getContentPane().setLayout(null);

		// Student input panel
		JPanel studentPanel = new JPanel();
		studentPanel.setBorder(new LineBorder(SystemColor.textHighlight, 5));
		studentPanel.setBounds(10, 10, 240, 395);
		studentPanel.setLayout(null);
		managementFrame.getContentPane().add(studentPanel);

		// Name field
		JLabel nameText = new JLabel(Translator.getValue("name"));
		nameText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameText.setBounds(10, 22, 67, 19);
		studentPanel.add(nameText);

		nameField = new JTextField();
		nameField.setName("nameField");
		nameField.setBounds(85, 23, 143, 22);
		nameField.setColumns(10);
		studentPanel.add(nameField);

		// Surname field
		JLabel surnameText = new JLabel(Translator.getValue("surname"));
		surnameText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		surnameText.setBounds(10, 54, 67, 19);
		studentPanel.add(surnameText);

		surnameField = new JTextField();
		surnameField.setName("surnameField");
		surnameField.setBounds(85, 51, 143, 22);
		surnameField.setColumns(10);
		studentPanel.add(surnameField);

		// Age field
		JLabel ageText = new JLabel(Translator.getValue("age"));
		ageText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ageText.setBounds(10, 86, 67, 19);
		studentPanel.add(ageText);

		ageField = new JTextField();
		ageField.setName("ageField");
		ageField.setBounds(85, 83, 143, 22);
		ageField.setColumns(10);
		studentPanel.add(ageField);

		// Gender selection
		JLabel genderText = new JLabel(Translator.getValue("gender"));
		genderText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		genderText.setBounds(10, 118, 67, 19);
		studentPanel.add(genderText);

		String[] genderOptions = (Gender.values() != null && Gender.values().length > 0)
				? java.util.Arrays.stream(Gender.values()).map(Enum::name).toArray(String[]::new)
				: new String[]{"Male", "Female", "Other"};
		genderSelectionBox = new JComboBox<>(new DefaultComboBoxModel<>(genderOptions));
		genderSelectionBox.setName("genderSelectionBox");
		genderSelectionBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		genderSelectionBox.setBounds(85, 115, 143, 22);
		studentPanel.add(genderSelectionBox);

		// Course selection
		JLabel courseText = new JLabel(Translator.getValue("course"));
		courseText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		courseText.setBounds(10, 150, 67, 19);
		studentPanel.add(courseText);

		courseSelectionBox = new JComboBox<>();
		courseSelectionBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		courseSelectionBox.setBounds(85, 147, 143, 22);
		updateCourses();
		studentPanel.add(courseSelectionBox);

		// Start date field
		JLabel startedDateText = new JLabel(Translator.getValue("started"));
		startedDateText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		startedDateText.setBounds(10, 182, 67, 19);
		studentPanel.add(startedDateText);

		startedDateField = new JTextField();
		startedDateField.setName("startedDateField");
		startedDateField.setBounds(85, 179, 143, 22);
		startedDateField.setColumns(10);
		startedDateField.setText(Translator.getValue("dateFormat"));
		studentPanel.add(startedDateField);

		// Add Faculty button
		JButton addFacultyButton = new JButton(Translator.getValue("addFaculty"));
		addFacultyButton.setName("addFacultyButton");
		addFacultyButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addFacultyButton.setBounds(10, 220, 220, 30);
		addFacultyButton.addActionListener(e -> {
			String facultyName = JOptionPane.showInputDialog(managementFrame, Translator.getValue("typeNameFaculty"));
			if (facultyName == null || facultyName.isEmpty()) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("emptyNameFaculty"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
			} else if (DBHandler.checkIfElementExists(DBHandler.getFacultiesTable(), facultyName)) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("facultyAlreadyExists"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
			} else {
				if (DBHandler.addFaculty(facultyName)) {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("facultySuccessfullyAdded"),
							Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("facultyNotAdded"),
							Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		studentPanel.add(addFacultyButton);

		// Add Course button
		JButton addCourseButton = new JButton(Translator.getValue("addCourse"));
		addCourseButton.setName("addCourseButton");
		addCourseButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addCourseButton.setBounds(10, 260, 220, 30);
		addCourseButton.addActionListener(e -> {
			if (DBHandler.getFaculties().length == 0) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("cannotAddCourse"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			String courseName = JOptionPane.showInputDialog(managementFrame, Translator.getValue("typeNameCourse"));
			if (courseName == null || courseName.isEmpty()) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("emptyNameCourse"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			String[] faculties = DBHandler.getFaculties();
			String faculty = (String) JOptionPane.showInputDialog(null, Translator.getValue("chooseFaculty"),
					Translator.getValue("sms"), JOptionPane.QUESTION_MESSAGE, null, faculties, faculties[0]);
			if (faculty == null || faculty.isEmpty()) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("courseNotAddedNoFaculty"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			int duration;
			try {
				duration = Integer.parseInt(JOptionPane.showInputDialog(managementFrame,
						Translator.getValue("courseTypeDuration")));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("courseNotAddedNoDuration"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (DBHandler.checkIfElementExists(DBHandler.getCoursesTable(), courseName)) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("courseAlreadyExists"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
			} else if (DBHandler.addCourse(courseName, faculty, duration)) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("courseSuccessfullyAdded"),
						Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
				updateCourses();
			} else {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("courseNotAdded"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
			}
		});
		studentPanel.add(addCourseButton);

		// Delete Faculty button
		JButton deleteFacultyButton = new JButton(Translator.getValue("deleteFaculty"));
		deleteFacultyButton.setName("deleteFacultyButton");
		deleteFacultyButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		deleteFacultyButton.setBounds(10, 300, 220, 30);
		deleteFacultyButton.addActionListener(e -> {
			table.clearSelection();
			String faculty = (String) JOptionPane.showInputDialog(null, Translator.getValue("sms"),
					Translator.getValue("chooseFacultyDelete"), JOptionPane.QUESTION_MESSAGE, null,
					DBHandler.getFaculties(), DBHandler.getFaculties()[0]);
			if (faculty == null) return;

			if (DBHandler.getNumberOfCourses(faculty) > 0) {
				if (JOptionPane.showConfirmDialog(managementFrame, Translator.getValue("deleteFacultyWithCourses"),
						Translator.getValue("sms"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					if (DBHandler.deleteFacultyCourses(faculty)) {
						JOptionPane.showMessageDialog(managementFrame,
								Translator.getValue("coursesFromFacultySuccessfullyDeleted"),
								Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
						if (DBHandler.deleteFaculty(faculty)) {
							JOptionPane.showMessageDialog(managementFrame, Translator.getValue("facultyDeleted"),
									Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongTryAgain"),
									Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongTryAgain"),
								Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if (DBHandler.deleteFaculty(faculty)) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("facultyDeleted"),
						Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongTryAgain"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
			}
			updateCourses();
		});
		studentPanel.add(deleteFacultyButton);

		// Delete Course button
		JButton deleteCourseButton = new JButton(Translator.getValue("deleteCourse"));
		deleteCourseButton.setName("deleteCourseButton");
		deleteCourseButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		deleteCourseButton.setBounds(10, 340, 220, 30);
		deleteCourseButton.addActionListener(e -> {
			table.clearSelection();
			String course = (String) JOptionPane.showInputDialog(null, Translator.getValue("sms"),
					Translator.getValue("chooseCourseDelete"), JOptionPane.QUESTION_MESSAGE, null,
					DBHandler.getCourses(), DBHandler.getCourses()[0]);
			if (course == null) return;

			if (DBHandler.getNumberOfAttendees(DBHandler.getCoursesTable(), course) > 0) {
				if (JOptionPane.showConfirmDialog(managementFrame, Translator.getValue("deleteCourseWithStudents"),
						Translator.getValue("deleteCourse"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					if (DBHandler.deleteCourseAttendees(course)) {
						JOptionPane.showMessageDialog(managementFrame,
								Translator.getValue("studentsAttendingSuccessfullyDeleted"),
								Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
						if (DBHandler.deleteCourse(course)) {
							JOptionPane.showMessageDialog(managementFrame, Translator.getValue("courseDeleted"),
									Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongTryAgain"),
									Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongTryAgain"),
								Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if (DBHandler.deleteCourse(course)) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("courseDeleted"),
						Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongTryAgain"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
			}
			updateCourses();
		});
		studentPanel.add(deleteCourseButton);

		// Table panel to display student records
		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(new LineBorder(SystemColor.textHighlight, 5));
		tablePanel.setBounds(260, 10, 575, 395);
		tablePanel.setLayout(null);
		managementFrame.getContentPane().add(tablePanel);

		JScrollPane tableScrollPane = new JScrollPane();
		tableScrollPane.setBounds(10, 10, 555, 375);
		tablePanel.add(tableScrollPane);

		table = new JTable();
		tableScrollPane.setViewportView(table);
		table.setColumnSelectionAllowed(true);
		table.setModel(new DefaultTableModel(new Object[][]{},
				new String[]{Translator.getValue("ID"), Translator.getValue("name"), Translator.getValue("surname"),
						Translator.getValue("age"), Translator.getValue("gender"), Translator.getValue("course"),
						Translator.getValue("started"), Translator.getValue("graduation")}) {
			boolean[] columnEditables = new boolean[]{false, true, true, true, true, false, false, false};

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		TableRowSorter<DefaultTableModel> tableSorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
		table.setRowSorter(tableSorter);

		table.getModel().addTableModelListener(e -> {
			if (!DBHandler.updateDatabase()) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("checkInput"),
						Translator.getValue("sms"), JOptionPane.ERROR_MESSAGE);
			}
		});

		// Buttons panel for actions
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBorder(new LineBorder(new Color(0, 120, 215), 5));
		buttonsPanel.setBackground(UIManager.getColor("Button.background"));
		buttonsPanel.setBounds(10, 415, 825, 80);
		buttonsPanel.setLayout(new GridLayout(0, 5, 0, 0));
		managementFrame.getContentPane().add(buttonsPanel);

		// Add button
		JButton addButton = new JButton(Translator.getValue("add"));
		addButton.setName("addButton");
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addButton.addActionListener(e -> {
			table.clearSelection();
			if (DBHandler.getFaculties().length == 0) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("cannotAddStudent"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || ageField.getText().isEmpty()
					|| startedDateField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("fillEmptyFields"),
						Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					format.setLenient(false);
					format.parse(startedDateField.getText());
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("dateFormatError"),
							Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (DBHandler.addStudent()) {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("studentSuccessfullyAdded"),
							Translator.getValue("success"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongInput"),
							Translator.getValue("error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonsPanel.add(addButton);

		// Update button
		JButton updateButton = new JButton(Translator.getValue("update"));
		updateButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		updateButton.addActionListener(e -> {
			table.clearSelection();
			DBHandler.updateStudents();
		});
		buttonsPanel.add(updateButton);

		// Delete button
		JButton deleteButton = new JButton(Translator.getValue("delete"));
		deleteButton.setName("deleteButton");
		deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		deleteButton.addActionListener(e -> {
			if (table.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(managementFrame, Translator.getValue("noStudentSelected"),
						Translator.getValue("sms"), JOptionPane.ERROR_MESSAGE);
			} else if (JOptionPane.showConfirmDialog(managementFrame, Translator.getValue("warningDelete"),
					Translator.getValue("sms"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				if (DBHandler.deleteStudent()) {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("studentSuccessfullyDeleted"),
							Translator.getValue("sms"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(managementFrame, Translator.getValue("somethingWrongUnexpected"),
							Translator.getValue("sms"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonsPanel.add(deleteButton);

		// Disconnect button
		JButton disconnectButton = new JButton(Translator.getValue("disconnect"));
		disconnectButton.setName("disconnectButton");
		disconnectButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		disconnectButton.addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(managementFrame, Translator.getValue("confirmDialog"),
					Translator.getValue("sms"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				ConnectionView.main(null);
				managementFrame.dispose();
			}
		});
		buttonsPanel.add(disconnectButton);

		// Exit button
		JButton exitButton = new JButton(Translator.getValue("exit"));
		exitButton.setName("exitButton");
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		exitButton.addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(managementFrame, Translator.getValue("confirmDialog"),
					Translator.getValue("sms"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				managementFrame.dispose();
				System.exit(0);
			}
		});
		buttonsPanel.add(exitButton);
	}
}