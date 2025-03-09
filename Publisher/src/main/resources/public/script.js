document.getElementById("publishButton").addEventListener("click", function() {
    const topic = document.getElementById("topic").value;
    const message = document.getElementById("message").value;
    const publisherId = document.getElementById("publisherId").value;

    if (topic === "Select Topic" || !message.trim()) {
        alert("Please select a topic and enter a message.");
        return;
    }

    const requestData = {
        id: Math.floor(Math.random() * 100000), // Integer ID
        topic: topic,
        message: message,
        pid: publisherId,
        mid: Math.floor(Math.random() * 10000), // Random message ID
        timestamp: new Date().toISOString()
    };
    const API_BASE_URL = `${window.location.protocol}//${window.location.hostname}:8080`;

    console.log("API Base URL:", API_BASE_URL);

    fetch(`${API_BASE_URL}/publisher/publish`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
    })
        .then(response => response.json())
        .then(data => {
            console.log("Message Sent:", data);
            addMessageToTable(requestData);
            document.getElementById("message").value = "";
        })
        .catch(error => console.error("Error:", error));
});

function addMessageToTable(data) {
    const tableBody = document.querySelector("#messagesTable tbody");
    const row = tableBody.insertRow();
    row.insertCell(0).innerText = tableBody.rows.length;
    row.insertCell(1).innerText = data.topic;
    row.insertCell(2).innerText = data.message;
    row.insertCell(3).innerText = new Date().toLocaleTimeString();
}
