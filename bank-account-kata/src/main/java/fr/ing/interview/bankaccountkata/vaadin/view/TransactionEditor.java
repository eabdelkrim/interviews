package fr.ing.interview.bankaccountkata.vaadin.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import fr.ing.interview.bankaccountkata.exception.AccountNotFoundException;
import fr.ing.interview.bankaccountkata.exception.InsufficientDepositException;
import fr.ing.interview.bankaccountkata.exception.InsufficientFundException;
import fr.ing.interview.bankaccountkata.model.Transaction;
import fr.ing.interview.bankaccountkata.service.AccountService;
import fr.ing.interview.bankaccountkata.service.TransactionService;
import fr.ing.interview.bankaccountkata.vaadin.service.AsyncRestClientService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class TransactionEditor extends VerticalLayout implements KeyNotifier {

	private final AsyncRestClientService asyncRestClientService;

	/**
	 * The currently edited transaction
	 */
	private Transaction transaction;
	
	private AccountService accountService;

	/* Fields to edit properties in Transaction entity */
	BigDecimalField amount = new BigDecimalField("amount");
	TextField transactionTypeLabel = new TextField("transactionTypeLabel");
	TextField transactionDate = new TextField("transactionDate");

	/* Action buttons */
	// TODO why more code?
	Button deposit = new Button("Deposit", VaadinIcon.CHECK.create());
	Button withdrawal = new Button("Withdrawal", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	HorizontalLayout actions = new HorizontalLayout(deposit, withdrawal, cancel);

	Binder<Transaction> binder = new Binder<>(Transaction.class);
	private ChangeHandler changeHandler;

	@Autowired
	public TransactionEditor(@Autowired(required = true) AccountService accountService, AsyncRestClientService asyncRestClientService) {
		this.accountService = accountService;
		this.asyncRestClientService = asyncRestClientService;

		add(amount, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);

		deposit.getElement().getThemeList().add("primary");
		withdrawal.getElement().getThemeList().add("primary");

		addKeyPressListener(Key.ENTER, e -> deposit());
		addKeyPressListener(Key.ENTER, e -> withdrawal());
		// wire action buttons to deposit and withdrawal
		deposit.addClickListener(e -> deposit());
		withdrawal.addClickListener(e -> withdrawal());
		cancel.addClickListener(e -> {setVisible(false); changeHandler.onChange();});
		setVisible(false);
	}

	void withdrawal() {	
		try {
			this.setVisible(false);
			this.accountService.withdrawal(1000L, transaction.getAmount());
			changeHandler.onChange();
		} catch (InsufficientFundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void deposit() {
		try {
			this.setVisible(false);
			this.accountService.deposit(1000L, transaction.getAmount());
			changeHandler.onChange();
		} catch (InsufficientDepositException | AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editTransaction(Transaction t, String transactionType) {
		if (t == null) {
			setVisible(false);
			return;
		}
		
		if(MainView.DEPOSIT.equals(transactionType)) {
			withdrawal.setVisible(false);
			deposit.setVisible(true);
		} else if (MainView.WITHDRAWAL.equals(transactionType)) {
			deposit.setVisible(false);
			withdrawal.setVisible(true);
		}
		final boolean persisted = t.getId() != null;
		if (persisted) {
			 // Find fresh entity for editing
			//transaction = transactionService.findById(t.getId()).get();
		}
		else {
			transaction = t;
		}
		//cancel.setVisible(persisted);

		// Bind transaction properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(transaction);

		setVisible(true);

		// Focus first name initially
		amount.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

}

