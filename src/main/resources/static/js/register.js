 const form = document.querySelector("form");
    form.addEventListener("submit", (event) => {
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        if (password !== confirmPassword) {
            event.preventDefault();
            alert("Passwords do not match!");
        }
    });
