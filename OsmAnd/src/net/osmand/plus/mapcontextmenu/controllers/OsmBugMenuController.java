package net.osmand.plus.mapcontextmenu.controllers;

import android.graphics.drawable.Drawable;

import net.osmand.data.LatLon;
import net.osmand.data.PointDescription;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.OsmandPlugin;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.mapcontextmenu.MenuBuilder;
import net.osmand.plus.mapcontextmenu.MenuController;
import net.osmand.plus.osmedit.OsmBugsLayer.OpenStreetNote;
import net.osmand.plus.osmedit.OsmEditingPlugin;

public class OsmBugMenuController extends MenuController {

	private OsmEditingPlugin plugin;
	private OpenStreetNote bug;

	public OsmBugMenuController(OsmandApplication app, MapActivity mapActivity, PointDescription pointDescription, OpenStreetNote bug) {
		super(new MenuBuilder(app), pointDescription, mapActivity);
		plugin = OsmandPlugin.getPlugin(OsmEditingPlugin.class);
		this.bug = bug;

		leftTitleButtonController = new TitleButtonController() {
			@Override
			public void buttonPressed() {
				if (plugin != null) {
					plugin.getBugsLayer(getMapActivity()).commentBug(getBug());
				}
			}
		};
		leftTitleButtonController.caption = getMapActivity().getString(R.string.poi_dialog_comment);
		leftTitleButtonController.leftIconId = R.drawable.ic_action_note_dark;

		rightTitleButtonController = new TitleButtonController() {
			@Override
			public void buttonPressed() {
				if (plugin != null) {
					plugin.getBugsLayer(getMapActivity()).closeBug(getBug());
				}
			}
		};
		rightTitleButtonController.caption = getMapActivity().getString(R.string.shared_string_close);
		rightTitleButtonController.leftIconId = R.drawable.ic_action_remove_dark;

		updateData();
	}

	@Override
	protected void setObject(Object object) {
		if (object instanceof OpenStreetNote) {
			this.bug = (OpenStreetNote) object;
			updateData();
		}
	}

	public OpenStreetNote getBug() {
		return bug;
	}

	@Override
	protected int getSupportedMenuStatesPortrait() {
		return MenuState.HEADER_ONLY | MenuState.HALF_SCREEN | MenuState.FULL_SCREEN;
	}

	@Override
	public Drawable getLeftIcon() {
		if (bug.isOpened()) {
			return getIcon(R.drawable.ic_action_gabout_dark, R.color.osm_bug_unresolved_icon_color);
		} else {
			return getIcon(R.drawable.ic_action_gabout_dark, R.color.osm_bug_resolved_icon_color);
		}
	}

	@Override
	public String getTypeStr() {
		return getPointDescription().getTypeName();
	}

	@Override
	public boolean needStreetName() {
		return false;
	}

	@Override
	public void addPlainMenuItems(String typeStr, PointDescription pointDescription, LatLon latLon) {
		super.addPlainMenuItems(typeStr, pointDescription, latLon);
		for (String description : bug.getCommentDescriptionList()) {
			addPlainMenuItem(R.drawable.ic_action_note_dark, description, true);
		}
	}

	@Override
	public void updateData() {
		rightTitleButtonController.visible = bug.isOpened();
	}
}