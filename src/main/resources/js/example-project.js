/**
 * Created by emorenkov on 03.09.2015.
 */
var Morenkov = Morenkov || {};
var $ = AJS.$;

Morenkov.AdminPage = (function () {

    /*
     Restful table views
     */
    PropertyEditView = AJS.RestfulTable.EditRow.extend({

        initialize: function () {
            AJS.RestfulTable.EditRow.prototype.initialize.apply(this, arguments);
            var self = this;

            //makes the model an Event Bus in order to subscribe to row RENDER event in fields CustomEditView
            self.bind(AJS.RestfulTable.Events.RENDER,  function () {
                self.model.trigger(AJS.RestfulTable.Events.RENDER, arguments);
            });

            this.bind(AJS.RestfulTable.Events.VALIDATION_ERROR, function (errors) {
                console.log(errors);
                AJS.messages.error("#property-validation-messages", {
                    title: "Validation Error",
                    body: "<pre class='validation-error-body'>" + errors + "</pre>",
                    fadeout: true
                });
            });
        }
    });

    DescriptionEditView = AJS.RestfulTable.CustomEditView.extend({
        render: function (args) {
            var value = this.model.get(args.name);
            var textarea = AJS.$("<textarea class='textarea' style='max-height: 150px; width: 250px;'></textarea>").attr({
                name: args.name,
                value: value
            });
            textarea.val(value);
            this.setElement(textarea);

            return this.el;
        }
    });

    var DescriptionReadView = AJS.RestfulTable.CustomEditView.extend({
        render: function (args) {
            var span = $("<span>" + args.value + "<span>");
            this.setElement(span);
            return this.$el;
        }
    });

    function addPropertyConfigTable() {
        var configRest = contextPath + "/rest/adminpage-example/1.0/config";
        var propertyConfigTable = new AJS.RestfulTable({
            autoFocus: true,
            el: jQuery("#property-config-table"),
            allowReorder: false,
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
                    emptyText: AJS.I18n.getText("morenkov.administtration-section.admin.display-name")
                },
                {
                    id: "description",
                    header: AJS.I18n.getText("morenkov.administtration-section.admin.description"),
                    emptyText: AJS.I18n.getText("morenkov.administtration-section.admin.description"),
                    readView: DescriptionReadView,
                    editView: DescriptionEditView
                }
            ],
            views: {
                editRow: PropertyEditView,
                row: AJS.RestfulTable.Row
            }
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
