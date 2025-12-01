package com.main.core.entity.history;


import com.main.core.helper.CurrentRequest;
import com.main.entity.User;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;
import com.main.core.Setup;

public class HistoryListener implements EntityTrackingRevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        HistoryRevision entity = (HistoryRevision) revisionEntity;
        if (Setup.isFinish()
                && CurrentRequest.isExist()) {
            User user = CurrentRequest.getUser();
            if (user != null) {
                entity.setCreator(user
                        .toString());
            }
        }
    }

    @Override
    public void entityChanged(Class entityClass, String entityName, Object entityId, RevisionType revisionType, Object revisionEntity) {
        // Nothing to be updated at now
    }
}
