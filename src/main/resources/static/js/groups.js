$(document).ready(function() {
    $('.groups-sort').on('change', function() {
        var selectedOption = $(this).val();
        if (selectedOption) {
            // Redirect to /rest/search with the selected option as a query parameter
            window.location.href = '/groups?orderCondition=' + selectedOption;
        }
    });
});