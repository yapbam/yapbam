package net.yapbam.data.persistence;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.framework.Application;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;

@SuppressWarnings("serial")
public class URIChooser extends JTabbedPane {
	/**
	 * Create the panel.
	 */
	public URIChooser() {
		setTabPlacement(JTabbedPane.TOP);
		for (int i = 0; i < PersistenceManager.MANAGER.getPluginsNumber(); i++) {
			PersistencePlugin plugin = PersistenceManager.MANAGER.getPlugin(i);
			Component uiChooser = (Component) plugin.buildChooser();
			addTab(plugin.getName(), plugin.getIcon(), uiChooser, plugin.getTooltip());
			uiChooser.addPropertyChangeListener(AbstractURIChooserPanel.SELECTED_URI_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					firePropertyChange(AbstractURIChooserPanel.SELECTED_URI_PROPERTY, evt.getOldValue(), evt.getNewValue());
				}
			});
		}
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						refresh();
					}
				});
			}
		});
	}

	private void setDialogType(boolean save) {
		for (int i = 0; i < this.getTabCount(); i++) {
			AbstractURIChooserPanel tab = (AbstractURIChooserPanel)this.getComponentAt(i);
			tab.setDialogType(save);
		}
	}
	
	public URI showOpenDialog(Component parent) {
		setDialogType(false);
		return showDialog(parent);
	}
	
	public URI showSaveDialog(Component parent) {
		setDialogType(true);
		return showDialog(parent);
	}
	
	private URI showDialog(Component parent) {
		Window owner = Utils.getOwnerWindow(parent);
		final URIChooserDialog dialog = new URIChooserDialog(owner, "URI Chooser", this);
		dialog.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowOpened(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowOpened(WindowEvent e) {
				refresh();
			}
		});
		dialog.setVisible(true);
		return dialog.getResult();
	}

  private void refresh() {
		AbstractURIChooserPanel panel = (AbstractURIChooserPanel)getSelectedComponent();
		if (panel!=null) panel.refresh();
	}

	public URI getSelectedURI() {
		AbstractURIChooserPanel panel = (AbstractURIChooserPanel)getSelectedComponent();
		return panel.getSelectedURI();
	}

	private static final class MyApp extends Application {
		@Override
		protected Container buildMainPanel() {
			JPanel result = new JPanel();
			JButton button = new JButton("Open");
			result.add(button);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println (new URIChooser().showOpenDialog(getJFrame()));
				}
			});
			JButton saveButton = new JButton("Save");
			result.add(saveButton);
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println (new URIChooser().showSaveDialog(getJFrame()));
				}
			});
			return result;
		}
	}
	
	public static void main(String[] args) {
		Application app = new MyApp();
		app.launch();
	}
}
