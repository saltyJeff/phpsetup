<html>
    <head>
        <title>TODO demo</title>
		<style>
			table, tr, td, th {
				border: 1px solid black;
				border-collapse: collapse;
			}
		</style>
    </head>
    <body>
		<h1>Add task</h1>
		<form method="post" action="#">
			<p>Task: </p>
			<textarea name="task" style="width: 300px; height: 300px;"></textarea>
			<br>
			<button type="submit" value="Submit">Submit</button>
			<button type="reset" value="Reset">Reset</button>
		</form>
		<h1>Tasks</h1>
        <?php
			$db = new SQLite3("todos.db");
			$db->query("CREATE TABLE IF NOT EXISTS todos (id INTEGER PRIMARY KEY, task TEXT);");
			if (isset($_POST["task"])) {
				$db->query("INSERT INTO todos (task) VALUES('" . $_POST["task"] . "')");
			}
			else if (isset($_POST["del"])) {
				$db->query("DELETE FROM todos WHERE id=".$_POST["del"]);
			}
			$results = $db->query("SELECT * FROM todos");
		?>
		<table>
			<tr>
				<th>ID</th>
				<th style="width: 300px">Task</th>
				<th>Delete</th>
			</tr>
			<?php
				while($row = $results->fetchArray()){
					echo "<tr>";
					echo "<td>".$row[0]."</td>";
					echo "<td>".$row[1]."</td>";
					echo "<td>";
					echo "<button onclick='handleDel(".$row[0].")'>X</button>";
					echo "</td>";
					echo "</tr>";
				}
			?>
		</table>
		<form method="post" action="#" style="display: none" id="delForm">
			<input name="del" id="delInput"/>
		</form>
		<script>
			var delForm = document.querySelector('#delForm');
			var delInput = document.querySelector('#delInput');
			function handleDel(i) {
				delInput.value = i;
				delForm.submit();
			}
		</script>
    </body>
</html>