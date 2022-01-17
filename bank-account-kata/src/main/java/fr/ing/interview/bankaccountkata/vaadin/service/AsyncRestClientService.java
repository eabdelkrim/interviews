package fr.ing.interview.bankaccountkata.vaadin.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import fr.ing.interview.bankaccountkata.model.Account;
import fr.ing.interview.bankaccountkata.model.Transaction;
import fr.ing.interview.bankaccountkata.security.jwt.model.JwtRequest;
import fr.ing.interview.bankaccountkata.security.jwt.model.JwtResponse;
import reactor.core.publisher.Mono;

@Component
public class AsyncRestClientService {

	public static interface AsyncRestCallback<T> {
		void operationFinished(T results);
	}

	public void getAccountTransactionsAsync(AsyncRestCallback<List<Transaction>> callback) {
		WebClient.create().post().uri("http://localhost:8080/authenticate")
		.body(Mono.just(new JwtRequest("bank_user", "password")), JwtRequest.class)
		.retrieve().toEntity(String.class).subscribe(result -> {
			String token = result.getBody();
			// Configure fetch as normal
			WebClient webClient = WebClient.builder().defaultHeader("Authorization", "Bearer "+ token).build();
			RequestHeadersSpec<?> spec = webClient.get().uri("http://localhost:8080/api/accounts/1000/transactions");
			//request.AddHeader("Authorization", $"Bearer {token}");
			// But instead of 'block', do 'subscribe'. This means the fetch will run on a
			// separate thread and notify us when it's ready by calling our lambda
			// operation.
			spec.retrieve().toEntityList(Transaction.class).subscribe(response -> {

				// get results as usual
				final List<Transaction> transactions = response.getBody();

				// call the ui with the data
				callback.operationFinished(transactions);
			});
		});
		
	}
	
	public void getAccountAsync(AsyncRestCallback<Account> callback) {

		// Configure fetch as normal
		RequestHeadersSpec<?> spec = WebClient.create().get().uri("http://localhost:8080/api/accounts/1000");

		// But instead of 'block', do 'subscribe'. This means the fetch will run on a
		// separate thread and notify us when it's ready by calling our lambda
		// operation.
		spec.retrieve().toEntity(Account.class).subscribe(result -> {

			// get results as usual
			final Account account = result.getBody();

			// call the ui with the data
			callback.operationFinished(account);
		});
	}
	
	public void postDepositTransactionAsync(AsyncRestCallback<List<Transaction>> callback, Transaction transaction) {

		// Configure fetch as normal
		//Transaction t = RequestHeadersSpec<?> spec 
		RequestHeadersSpec<?> spec = WebClient.create().post()
				.uri("http://localhost:8080/api/accounts/1000/deposit")
				.body(Mono.just(transaction), Transaction.class);

		// But instead of 'block', do 'subscribe'. This means the fetch will run on a
		// separate thread and notify us when it's ready by calling our lambda
		// operation.
		spec.retrieve().toEntityList(Transaction.class).subscribe(result -> {

			// get results as usual
			final List<Transaction> transactions = result.getBody();

			// call the ui with the data
			callback.operationFinished(transactions);
		});
	}

	public void postWithdrawalTransactionAsync(AsyncRestCallback<List<Transaction>> callback, Transaction transaction) {
		// Configure fetch as normal
				//Transaction t = RequestHeadersSpec<?> spec 
				RequestHeadersSpec<?> spec = WebClient.create().post()
						.uri("http://localhost:8080/api/accounts/1000/withdrawal")
						.body(Mono.just(transaction), Transaction.class);

				// But instead of 'block', do 'subscribe'. This means the fetch will run on a
				// separate thread and notify us when it's ready by calling our lambda
				// operation.
				spec.retrieve().toEntityList(Transaction.class).subscribe(result -> {

					// get results as usual
					final List<Transaction> transactions = result.getBody();

					// call the ui with the data
					callback.operationFinished(transactions);
				});
		
	}
}
