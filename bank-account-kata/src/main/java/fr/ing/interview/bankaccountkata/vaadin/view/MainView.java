package fr.ing.interview.bankaccountkata.vaadin.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import fr.ing.interview.bankaccountkata.model.Transaction;
import fr.ing.interview.bankaccountkata.model.TransactionType;
import fr.ing.interview.bankaccountkata.repository.TransactionRepository;
import fr.ing.interview.bankaccountkata.service.AccountService;
import fr.ing.interview.bankaccountkata.service.TransactionService;
import fr.ing.interview.bankaccountkata.vaadin.model.TransactionModel;
import fr.ing.interview.bankaccountkata.vaadin.service.AsyncRestClientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
@Route
public class MainView extends VerticalLayout {

	private final AsyncRestClientService asyncRestClientService;
	
	private AccountService accountService;

	private final TransactionEditor transactionEditor;

	final Grid<Transaction> transactionGrid;

	final TextField balance;

	public static final String DEPOSIT = "DEPOSIT";

	public static final String WITHDRAWAL = "WITHDRAWAL";

	private final Button depositButton;
	
	private final Button withdrawalButton;

	public MainView(@Autowired(required = true)AccountService accountService, @Autowired(required = true) AsyncRestClientService asyncRestClientService,
			TransactionEditor transactionEditor) {
		this.asyncRestClientService = asyncRestClientService;
		this.accountService = accountService;
		this.transactionEditor = transactionEditor;
		this.transactionGrid = new Grid<>(Transaction.class);
		this.balance = new TextField("Balance");
		this.depositButton = new Button("Deposit", VaadinIcon.PLUS.create());
		this.withdrawalButton = new Button("Withdrawal", VaadinIcon.PLUS.create());
		// build layout
		HorizontalLayout actions = new HorizontalLayout(balance, depositButton, withdrawalButton);
		add(actions, transactionGrid, transactionEditor);

		transactionGrid.setHeight("300px");
		//transactionGrid.setColumns(new LocalDateRenderer<Transaction>(MainView::getTransactionDate,
		//        "yyyy-MM-dd")).setHeader("Transaction Date").setSortable(true)
		 //       .setComparator(Transaction::getDate).setWidth("300px").setFlexGrow(0)
		transactionGrid.setColumns("date", "amount", "transactionTypeLabel", "currentBalance");
		transactionGrid.getColumnByKey("date").setWidth("300px").setFlexGrow(0);
		//transactionGrid.getColumnByKey("date").setRenderer((Renderer) new DateRenderer("%1$td/%1$tm/%1$tY"));
	
		// Instantiate and edit new Transaction when the deposit or the withdrawal
		// button is clicked
		depositButton.addClickListener(e -> transactionEditor.editTransaction(
				new Transaction(new Date(), TransactionType.DEPOSIT, null, null, null),
				DEPOSIT));
		withdrawalButton.addClickListener(e -> transactionEditor.editTransaction(new Transaction(new Date(),
				TransactionType.WITHDRAWAL, null, null, null), WITHDRAWAL));			
		asyncRestClientService.getAccountTransactionsAsync(result -> {
			getUI().get().access(() -> {
				transactionGrid.setEnabled(true);
				transactionGrid.setItems(result);
				this.balance.setValue(result.get(0).getAccount().getBalance().toString());
			});
		});
		
		// Listen changes made by the editor, refresh data from backend
		transactionEditor.setChangeHandler(() -> {
			listTransactions();
		});

		// Initialize listing
		listTransactions();
	}

	void listTransactions() {
		List<Transaction> transactions = accountService.findAccountAllTransactions(1000L);
		transactionGrid.setItems(transactions);
		this.balance.setValue(transactions.get(0).getAccount().getBalance().toString());
	}
}
