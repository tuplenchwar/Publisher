document.getElementById("publishButton").addEventListener("click", function() {
    const topic = document.getElementById("topic").value;
    const message = document.getElementById("message").value;
    const publisherId = document.getElementById("publisherId").value;

    if (topic === "Select Topic" || !message.trim()) {
        alert("Please select a topic and enter a message.");
        return;
    }

    const requestData = {
        id: Math.floor(Math.random() * 100000), // Ensuring a valid integer ID
        topic: topic,
        message: message,
        pid: publisherId,
        mid: Math.floor(Math.random() * 10000), // Random message ID as integer
        timestamp: new Date().toISOString()
    };

    fetch("http://localhost:8070/publisher/publish", {
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
    const table = document.getElementById("messagesTable");
    const row = table.insertRow(-1);
    row.insertCell(0).innerText = table.rows.length - 1;
    row.insertCell(1).innerText = data.topic;
    row.insertCell(2).innerText = data.message;
    row.insertCell(3).innerText = new Date().toLocaleTimeString();
}
