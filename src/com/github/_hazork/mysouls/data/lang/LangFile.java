package com.github._hazork.mysouls.data.lang;

import com.github._hazork.mysouls.SoulsPlugin;
import com.github.arthurfiorette.sinklibrary.components.SinkPlugin;

public class LangFile extends LanguageFile<LangEnum> {

  private static LangFile instance = null;

  private LangFile(final SinkPlugin plugin) {
    super(plugin, plugin.getDataFolder(), "lang");
  }

  public static LangFile get() {
    return (LangFile.instance == null)
      ? LangFile.instance = new LangFile(SoulsPlugin.get())
      : LangFile.instance;
  }

  @Override
  protected String path(final LangEnum lang) {
    this.asList(lang, replacer -> replacer.add("", "").add("", ""));
    return lang.getPath();
  }
}
