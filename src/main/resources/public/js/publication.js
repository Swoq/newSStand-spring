$('#exampleModal').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget)
    let rateId = button.data('rate')
    let modal = $(this)
    modal.find('#hiddenRateInput').attr("value", rateId);
})