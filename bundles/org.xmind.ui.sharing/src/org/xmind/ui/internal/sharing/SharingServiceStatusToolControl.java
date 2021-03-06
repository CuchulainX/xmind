package org.xmind.ui.internal.sharing;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.util.Util;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.xmind.core.sharing.ILocalSharedLibrary;
import org.xmind.core.sharing.ISharingListener;
import org.xmind.core.sharing.ISharingService;
import org.xmind.core.sharing.SharingEvent;
import org.xmind.ui.resources.FontUtils;
import org.xmind.ui.util.PrefUtils;

public class SharingServiceStatusToolControl implements ISharingListener{
    
    private static final String ICON_ONLINE = "icons/online.gif"; //$NON-NLS-1$

    private static final String ICON_OFFLINE = "icons/offline.gif"; //$NON-NLS-1$

    private ISharingService sharingService;

    private Composite container = null;

    private Display display = null;

    private Menu menu = null;

    private MenuItem offlineItem = null;

    private MenuItem onlineItem = null;

    private Map<String, Image> icons = null;

    private Label stateTextLabel;

    private Label stateImageLabel;
    
	@Inject
	public SharingServiceStatusToolControl() {
	    this.sharingService = LocalNetworkSharingUI.getDefault()
                .getSharingService();
        if (sharingService != null) {
            sharingService.addSharingListener(this);
        }
	}
	
	@PostConstruct
	public void createControl(Composite parent) {
	    
	    display = Display.getCurrent();

        if (menu != null) {
            menu.dispose();
            menu = null;
        }
        if (container != null) {
            container.dispose();
            container=null;
        }

        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;
        container.setLayout(layout);
        
        Listener showMenuListener = new Listener() {
            public void handleEvent(Event event) {
                showMenu();
            }
        };
        stateImageLabel = new Label(container, SWT.RIGHT);
        stateImageLabel.setLayoutData(new GridData(SWT.FILL));
        stateImageLabel.addListener(SWT.MouseDown, showMenuListener);
        
        stateTextLabel = new Label(container, SWT.CENTER);
        stateTextLabel.setFont(FontUtils.getRelativeHeight(
                JFaceResources.DEFAULT_FONT, Util.isMac() ? -2 : -1));
        stateTextLabel.setLayoutData(new GridData(SWT.FILL));
        stateTextLabel.addListener(SWT.MouseDown, showMenuListener);
        
        update(null);
		
	}
	
	 private void update(String id) {
	        final ISharingService sharingService = this.sharingService;
	        if (sharingService != null) {
	            ILocalSharedLibrary localLibrary = sharingService.getLocalLibrary();
	            int status = sharingService.getStatus();
	            String localName = localLibrary.getName();
	            
	            if(stateTextLabel != null && !stateTextLabel.isDisposed()){
	                stateTextLabel.setText(localName);
	                stateTextLabel.setToolTipText(NLS
	                        .bind(SharingMessages.SharingServiceStatusItem_tooltip_withLocalUserName,
	                                localName));
	            }
	            if(stateImageLabel != null && !stateImageLabel.isDisposed()){
	                if (status == ISharingService.ACTIVE) {
	                    stateImageLabel.setImage(getIcon(ICON_ONLINE));
	                } else {
	                    stateImageLabel.setImage(getIcon(ICON_OFFLINE));
	                }
	                stateImageLabel.setToolTipText(NLS
	                        .bind(SharingMessages.SharingServiceStatusItem_tooltip_withLocalUserName,
	                                localName));
	            }

	        }
	        updateSize();
	    }
	 
	 private void updateSize() {
	        if (container == null || container.isDisposed())
	            return;
	        
	        Point oldSize = container.getSize();
	        container.pack(true);
	        Point newSize = container.getSize();
	        if(oldSize.equals(newSize))
	            return;
	        container.getParent().layout(true, true);
	        
	    }

	    @PreDestroy
	     void dispose() {
	        if (sharingService != null) {
	            sharingService.removeSharingListener(this);
	            sharingService = null;
	        }

	        if (menu != null) {
	            if (!menu.isDisposed())
	                menu.dispose();
	            menu = null;
	        }
	        offlineItem = null;
	        onlineItem = null;

	        if (container != null) {
	            if (!container.isDisposed())
	                container.dispose();
	            container = null;
	        }

	        if (icons != null) {
	            Object[] iconsToDispose = icons.values().toArray();
	            icons.clear();
	            for (int i = 0; i < iconsToDispose.length; i++) {
	                ((Image) iconsToDispose[i]).dispose();
	            }
	            icons = null;
	        }
	    }

	    private void showMenu() {
	        if(stateTextLabel==null||stateTextLabel.isDisposed())
	            return ;

	        if (menu == null || menu.isDisposed()) {
	            menu = new Menu(stateTextLabel);
	            fillMenu();
	        }
	        updateMenu();
	        Rectangle itemBounds = stateTextLabel.getBounds();
	        Point menuLoc = stateTextLabel.getParent().toDisplay(itemBounds.x, itemBounds.y);
	        menu.setLocation(menuLoc.x, menuLoc.y);
	        menu.setVisible(true);
	    }

	    private void fillMenu() {
	        MenuItem showViewItem = new MenuItem(menu, SWT.PUSH);
	        showViewItem
	                .setText(SharingMessages.SharingServiceStatusItem_ShowLocalNetworkSharingViewAction_text);
	        showViewItem.addListener(SWT.Selection, new Listener() {
	            public void handleEvent(Event event) {
	                showView();
	            }
	        });

	        new MenuItem(menu, SWT.SEPARATOR);

	        offlineItem = new MenuItem(menu, SWT.CHECK);
	        offlineItem
	                .setText(SharingMessages.SharingServiceStatusItem_GoOfflineAction_text);
	        offlineItem.addListener(SWT.Selection, new Listener() {
	            public void handleEvent(Event event) {
	                toggleServiceStatus(false);
	            }
	        });

	        onlineItem = new MenuItem(menu, SWT.CHECK);
	        onlineItem
	                .setText(SharingMessages.SharingServiceStatusItem_GoOnlineAction_text);
	        onlineItem.addListener(SWT.Selection, new Listener() {
	            public void handleEvent(Event event) {
	                toggleServiceStatus(true);
	            }
	        });

	        new MenuItem(menu, SWT.SEPARATOR);

	        MenuItem preferenceItem = new MenuItem(menu, SWT.PUSH);
	        preferenceItem
	                .setText(SharingMessages.SharingServiceStatusItem_ShowPreferencesAction_text);
	        preferenceItem.addListener(SWT.Selection, new Listener() {
	            public void handleEvent(Event event) {
	                showPreferences();
	            }
	        });
	    }

	    private void updateMenu() {
	        if (sharingService == null)
	            return;

	        int status = sharingService.getStatus();
	        if (offlineItem != null && !offlineItem.isDisposed()) {
	            offlineItem.setSelection(status == ISharingService.INACTIVE);
	        }
	        if (onlineItem != null && !onlineItem.isDisposed()) {
	            onlineItem.setSelection(status == ISharingService.ACTIVE);
	        }
	    }

	    private Image getIcon(String path) {
	        if(stateImageLabel==null||stateImageLabel.isDisposed())
	            return null;
	        
	        if (icons == null)
	            icons = new HashMap<String, Image>(2);
	        Image icon = icons.get(path);
	        if (icon == null || icon.isDisposed()) {
	            icon = LocalNetworkSharingUI.imageDescriptorFromPlugin(
	                    LocalNetworkSharingUI.PLUGIN_ID, path).createImage(false,
	                            stateImageLabel.getDisplay());
	            icons.put(path, icon);
	        }
	        return icon;
	    }

	    private void toggleServiceStatus(boolean online) {
	        if (sharingService == null)
	            return;

	        ToggleSharingServiceStatusJob.startToggle(sharingService, online, null,
	                false);
	    }

	    private void showView() {
	        IWorkbenchWindow window = PlatformUI.getWorkbench()
	                .getActiveWorkbenchWindow();
	        if (window == null)
	            return;

	        final IWorkbenchPage page = window.getActivePage();
	        if (page == null)
	            return;

	        SafeRunner.run(new SafeRunnable() {
	            public void run() throws Exception {
	                page.showView(LocalNetworkSharingUI.VIEW_ID);
	            }
	        });
	    }

	    private void showPreferences() {
	        IWorkbenchWindow window = PlatformUI.getWorkbench()
	                .getActiveWorkbenchWindow();
	        if (window == null)
	            return;

	        PrefUtils.openPrefDialog(window.getShell(),
	                LocalNetworkSharingUI.PREF_PAGE_ID);
	    }

	    private void asyncUpdate() {
	        if (display == null || display.isDisposed())
	            return;

	        display.asyncExec(new Runnable() {
	            public void run() {
	                update(null);
	            }
	        });
	    }

	    public void handleSharingEvent(SharingEvent event) {
	        if (event.getType() == SharingEvent.Type.LIBRARY_ADDED
	                || event.getType() == SharingEvent.Type.LIBRARY_REMOVED
	                || event.getType() == SharingEvent.Type.LIBRARY_NAME_CHANGED
	                || event.getType() == SharingEvent.Type.SERVICE_STATUS_CHANGED) {
	            asyncUpdate();
	        }
	    }

}