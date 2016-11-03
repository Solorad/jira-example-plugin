/**
 * Created by emorenkov on 03.09.2015.
 */
var Morenkov = Morenkov || {};
var $ = AJS.$;

Morenkov.AdminPage = (function () {

    function addPropertyConfigTable() {
        var configRest = contextPath + "/rest/adminpage-example/1.0/config";
        var propertyConfigTable = new AJS.RestfulTable({
            autoFocus: true,
            el: jQuery("#property-config-table"),
            allowReorder: true,
            noEntriesMsg: "No properties were set",
            allowEdit: true,
            resources: {
                all: configRest,
                self: configRest
            },
            columns: [
                {
                    id: "propertyKey",
                    header: AJS.I18n.getText("morenkov.administtration-section.admin.property-key"),
                    emptyText: AJS.I18n.getText("morenkov.administtration-section.admin.property-key")
                },
                {
                    id: "displayName",
                    header: AJS.I18n.getText("morenkov.administtration-section.admin.display-name"),
                    emptyText: AJS.I18n.getText("solarwinds.comment-property.admin.display-name")
                },
                {
                    id: "value",
                    header: AJS.I18n.getText("morenkov.administtration-section.admin.value"),
                    emptyText: AJS.I18n.getText("morenkov.administtration-section.admin.value")
                }
            ]
        });
    }


    var init = function () {
        addPropertyConfigTable();
    };

    return {
        init : init
    }
})();

AJS.$(function(){
    Morenkov.AdminPage.init();
});
