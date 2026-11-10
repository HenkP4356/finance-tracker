const rand = new Intl.NumberFormat('en-ZA', {style: 'currency', currency: 'ZAR'});

function formatAmount(transaction) {
    const sign = transaction.type === 'EXPENSE' ? '-' : '+';
    return sign + rand.format(transaction.amount);
}

async function loadTransactions() {
    const response = await fetch('/api/transactions');
    const transactions = await response.json();
    transactions.sort((a, b) => b.transactionDate.localeCompare(a.transactionDate));

    const tbody = document.getElementById('transactions');
    tbody.replaceChildren();

    for (const transaction of transactions) {
        const row = document.createElement('tr');

        row.appendChild(createCell(transaction.transactionDate));
        row.appendChild(createCell(transaction.description));
        row.appendChild(createCell(transaction.category));

        const amountCell = createCell(formatAmount(transaction));
        amountCell.classList.add('amount', transaction.type === 'INCOME' ? 'income' : 'expense');
        row.appendChild(amountCell);

        row.appendChild(createCell(transaction.type));

        tbody.appendChild(row);
    }

    let income = 0;
    let expenses = 0;

    for (const transaction of transactions) {
        if (transaction.type === 'INCOME') {
            income += Number(transaction.amount);
        } else if (transaction.type === 'EXPENSE') {
            expenses += Number(transaction.amount);
        }
    }

    const balance = income - expenses;

    document.getElementById('total-income').textContent = rand.format(income);
    document.getElementById('total-expense').textContent = rand.format(expenses);
    document.getElementById('total-balance').textContent = rand.format(balance);
}

function createCell(text) {
    const cell = document.createElement('td');
    cell.textContent = text;
    return cell;
}

loadTransactions();