const stripe = Stripe('pk_test_51OU9oYG3807R4hwqMYu10IqlpmKqdfP3HkbdB6eVjS6rLB7f0y4xGBefyeZerMMksw9efBS0ugXCIGxvkYpq1nyW008CWyxm8f');
const paymentButton = document.querySelector('#paymentButton');
const updateButton = document.querySelector('#updateButton');

if (paymentButton) {
	paymentButton.addEventListener('click', () => {
		stripe.redirectToCheckout({
			sessionId: sessionId
		})
	});
}

if(updateButton){
	updateButton.addEventListener('click', () => {
		if(portalSessionUrl && portalSessionUrl !== "portalSessionUrl"){
			window.location.href = portalSessionUrl;
//			カスタマーポータルのURLにリダイレクト
		}else {
			console.log('カスタマーポータルセッションのURLが見つかりません');
		}
	});
}