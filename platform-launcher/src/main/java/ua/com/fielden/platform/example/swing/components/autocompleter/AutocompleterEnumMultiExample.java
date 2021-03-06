package ua.com.fielden.platform.example.swing.components.autocompleter;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.miginfocom.swing.MigLayout;
import ua.com.fielden.platform.basic.IValueMatcher;
import ua.com.fielden.platform.basic.autocompleter.EnumValueMatcher;
import ua.com.fielden.platform.swing.actions.Command;
import ua.com.fielden.platform.swing.components.smart.autocompleter.development.AutocompleterTextFieldLayer;
import ua.com.fielden.platform.swing.components.smart.autocompleter.renderer.development.MultiplePropertiesListCellRenderer;
import ua.com.fielden.platform.swing.components.textfield.UpperCaseTextField;
import ua.com.fielden.platform.swing.utils.SimpleLauncher;
import ua.com.fielden.platform.utils.Pair;

/**
 * <code>AutocompleterEnumMultiExample</> demonstrates the use of {@link EnumValueMatcher}.
 * 
 * @author TG Team
 * 
 */
public class AutocompleterEnumMultiExample {
    public static void main(final String[] args) throws Exception {
        for (final LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(laf.getName())) {
                UIManager.setLookAndFeel(laf.getClassName());
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // create an instance of the overlayable text field, which bill be used for attaching overlay components
                final IValueMatcher<DemoEnum> matcher = new EnumValueMatcher<DemoEnum>(DemoEnum.class) {
                    @Override
                    public List<DemoEnum> findMatches(final String value) {
                        try {
                            Thread.sleep(500);
                        } catch (final InterruptedException e) {
                        }

                        return super.findMatches(value);
                    }
                };
                final Set<String> highlightProps = new HashSet<String>();
                highlightProps.add("name()");
                final MultiplePropertiesListCellRenderer<DemoEnum> cellRenderer = new MultiplePropertiesListCellRenderer<DemoEnum>("name()", new Pair[] { new Pair<String, String>("string", "toString()") }, highlightProps);
                final AutocompleterTextFieldLayer<DemoEnum> autocompleter = new AutocompleterTextFieldLayer<DemoEnum>(new UpperCaseTextField(), matcher, DemoEnum.class, "name()", cellRenderer, "caption...", ";");
                cellRenderer.setAuto(autocompleter.getAutocompleter());

                autocompleter.setPropertyToHighlight("toString()", true);

                final JPanel panel = new JPanel(new MigLayout("fill"));
                panel.add(autocompleter, "growx, h 25!, w 150, wrap");
                panel.add(new JButton(new Command<Boolean>("Print values") {
                    @Override
                    protected Boolean action(final ActionEvent e) throws Exception {
                        for (final DemoEnum entity : autocompleter.values()) {
                            System.out.println(entity);
                        }
                        return true;
                    }
                }), "align right");

                SimpleLauncher.show("Autocompleter demo", panel);
            }
        });
    }
}
