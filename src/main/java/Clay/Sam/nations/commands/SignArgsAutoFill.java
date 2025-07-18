package Clay.Sam.nations.commands;

import Clay.Sam.nations.Utils.SignWriter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SignArgsAutoFill {



    public String[] nullArgsOpenSign(CommandSender sender, String title, String[] args) {

        String[] signText = new String[4];

        if(args.length == 0) {
            SignWriter signWriter = new SignWriter();
            signText = signWriter.getSignWriterText((Player) sender, title);

        }

        return new String[]{signText[0], signText[1]};

    }

}
