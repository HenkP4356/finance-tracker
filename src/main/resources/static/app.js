async function loadTransactions() {
    const response = await fetch('/api/transactions');
    const transactions = await response.json();

    const tbody = document.getElementById('transactions');
    tbody.replaceChildren();

    for (const transaction of transactions) {
        const row = document.createElement('tr');

        row.appendChild(createCell(transaction.transactionDate));
        row.appendChild(createCell(transaction.description));
        row.appendChild(createCell(transaction.category));
        row.appendChild(createCell(transaction.amount));
        row.appendChild(createCell(transaction.type));

        tbody.appendChild(row);
    }
}

function createCell(text) {
    const cell = document.createElement('td');
    cell.textContent = text;
    return cell;
}

loadTransactions();