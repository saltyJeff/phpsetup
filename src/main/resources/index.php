<html>
    <head>
        <title>TODO demo</title>
		<style>
			table, tr, td, th {
				border: 1px solid black;
				border-collapse: collapse;
			}
			textarea {
				height: 100px;
			}
		</style>
    </head>
    <body>
		<h1>Add task</h1>
		<form method="post" action="#">
			<p>Task: </p>
			<textarea name="task"></textarea>
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
			else if(isset($_POST["editId"]) && isset($_POST["editTask"])) {
				$db->query("UPDATE todos SET task='".$_POST["editTask"]."' WHERE id=".$_POST["editId"]);
			}
			$results = $db->query("SELECT * FROM todos");
		?>
		<table>
			<tr>
				<th>ID</th>
				<th style="width: 300px">Task</th>
				<th>Delete</th>
				<th>Edit</th>
			</tr>
			<?php
				while($row = $results->fetchArray()){
					$elemId = 'task'.$row[0];
					echo "<tr>";
					echo "<td>".$row[0]."</td>";
					echo "<td><textarea id='".$elemId."'>".$row[1]."</textarea></td>";
					echo "<td>";
					echo "<button onclick='handleDel(".$row[0].")'>X</button>";
					echo "</td>";
					echo "<td>";
					echo "<button onclick=\"handleEdit(".$row[0].", '".
						$elemId."')\">*</button>";
					echo "</td>";
					echo "</tr>";
				}
			?>
		</table>
		<form method="post" action="#" style="display: none" id="delForm">
			<input name="del" id="delInput" />
		</form>
		<form method="post" action="#" style="display: none" id="editForm">
			<input name="editId" id="editIdInput" />
			<textarea name="editTask" id="editTaskInput"></textarea>
		</form>
		<script>
			var delForm = document.querySelector('#delForm');
			var delInput = document.querySelector('#delInput');
			function handleDel(i) {
				delInput.value = i;
				delForm.submit();
			}
			var editForm = document.querySelector('#editForm');
			var editIdInput = document.querySelector('#editIdInput');
			var editTaskInput = document.querySelector('#editTaskInput');
			function handleEdit(i, t) {
				console.log(t);
				editIdInput.value = i;
				editTaskInput.value = document.querySelector('#'+t).value;
				editForm.submit();
			}
		</script>
    </body>
</html>