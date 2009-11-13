
package gui;

import javax.swing.JList;
import misc.TagIdListModel;

/**
 *
 * @author Scheinecker Thomas
 */
public class TagIdList extends JList {

    public TagIdList(TagIdListModel model) {
        super(model);
    }

    @Override
    public TagIdListModel getModel() {
        return (TagIdListModel) super.getModel();
    }
}
