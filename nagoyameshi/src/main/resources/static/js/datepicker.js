let maxDate = new Date();
maxDate = maxDate.setMounth(maxDate.getMonth() + 3);

datepicker('#visitDate', {
	locale: 'ja',
	minDate: 'today',
	maxDate: maxDate
});