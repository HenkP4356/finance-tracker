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

        const actionsCell = document.createElement('td');
        actionsCell.appendChild(createButton('Category', () => editCategory(transaction)));
        actionsCell.appendChild(createButton('Delete', () => deleteTransaction(transaction)));
        row.appendChild(actionsCell);

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

async function deleteTransaction(transaction) {
    if (!confirm(`Delete "${transaction.description}"?`)) {
        return;
    }

    await fetch(`/api/transactions/${transaction.id}`, { method: 'DELETE' });
    loadTransactions();
}

async function editCategory(transaction){
    const category = prompt('New Category: ', transaction.category);

    if (category === null || category === '') {
        return;
    }

    await fetch(`/api/transactions/${transaction.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            type: transaction.type,
            amount: transaction.amount,
            category: category.trim(),
            description: transaction.description,
            transactionDate: transaction.transactionDate
        })
    });

    loadTransactions();
}

function createCell(text) {
    const cell = document.createElement('td');
    cell.textContent = text;
    return cell;
}

function createButton(label, onClick) {
    const button = document.createElement('button');
    button.textContent = label;
    button.addEventListener('click', onClick);
    return button;
}

document.getElementById('import-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const file = document.getElementById('import-file').files[0];
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch('/api/transactions/import', {
        method: 'POST',
        body: formData
    });

    const result = await response.json();

    document.getElementById('import-message').textContent = `Imported ${result.imported} new transactions`;

    loadTransactions();
})

loadTransactions();