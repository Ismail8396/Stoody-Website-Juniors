// TODO: Reload the data without refreshing the page

$(function () {

    $("#searchFilterId").change(function () {

        let searchInputValue = $("#searchFilterId").val();

        $.ajax({
            url: "/api/language/search/" + searchInputValue,
            contentType : 'application/json',
            type: "GET",
            success: function (incomingData) {
                console.log(incomingData.response);

                $('#dataTable-rows').html(getHTMLRowsFromJson(incomingData.response));
            }
        });

    })

    $(".removeLanguageModel").click(function () {

        let languageModelLocale = $(this).attr("data-locale");
        let languageModelKey = $(this).attr("data-key");
        let languageModelContent = $(this).attr("data-content");

        $.ajax({
            url: "/api/language/delete/" + languageModelLocale + "/" + languageModelKey + "/" + languageModelContent,
            type: "POST",
            success: function (incomingData) {
                //$("#dataTable").replaceWith("user_panel/fragments/languages_list_fragment.html :: #languages_list_element");
                location.reload();
            }
        });

    })

    $(".addLanguageModel").click(function () {

        let languageModelLocale = $("#field_locale").val();
        let languageModelKey = $("#field_key").val();
        let languageModelContent = $("#field_content").val();

        $.ajax({
            url: "/api/language/post/" + languageModelLocale + "/" + languageModelKey + "/" + languageModelContent,
            type: "POST",
            success: function (incomingData) {
                //$("#dataTable").replaceWith("user_panel/fragments/languages_list_fragment.html :: languages_list_element");
                location.reload();
            }
        });

    })
})