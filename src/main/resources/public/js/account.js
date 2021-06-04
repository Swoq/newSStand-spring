$('#cancelModal').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget)
    let subscriptionId = button.data('subscription')
    let modal = $(this)
    modal.find('#hiddenSubscriptionInput').attr("value", subscriptionId);
})