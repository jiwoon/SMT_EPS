object Form1: TForm1
  Left = 60
  Top = 125
  Width = 1456
  Height = 827
  Align = alClient
  Caption = 'mainForm'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object dataGrid: TDBGrid
    Left = 0
    Top = 283
    Width = 1440
    Height = 505
    Align = alBottom
    DataSource = ds
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = 60
    Font.Name = #24494#36719#38597#40657
    Font.Style = [fsBold]
    ParentFont = False
    ReadOnly = True
    TabOrder = 0
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
    OnDrawColumnCell = dataGridDrawColumnCell
    Columns = <
      item
        Alignment = taCenter
        Expanded = False
        FieldName = 'lineseat'
        Font.Charset = ANSI_CHARSET
        Font.Color = clBlack
        Font.Height = -55
        Font.Name = #24494#36719#38597#40657
        Font.Style = [fsBold]
        Title.Alignment = taCenter
        Title.Caption = #31449#20301
        Title.Font.Charset = ANSI_CHARSET
        Title.Font.Color = clWindowText
        Title.Font.Height = -33
        Title.Font.Name = #24494#36719#38597#40657
        Title.Font.Style = []
        Width = 200
        Visible = True
      end
      item
        Alignment = taCenter
        Expanded = False
        FieldName = 'store_issue_result'
        Font.Charset = ANSI_CHARSET
        Font.Color = clGreen
        Font.Height = -55
        Font.Name = #24494#36719#38597#40657
        Font.Style = [fsBold]
        Title.Alignment = taCenter
        Title.Caption = #21457#26009
        Title.Font.Charset = ANSI_CHARSET
        Title.Font.Color = clWindowText
        Title.Font.Height = -33
        Title.Font.Name = #24494#36719#38597#40657
        Title.Font.Style = []
        Width = 204
        Visible = True
      end
      item
        Alignment = taCenter
        Expanded = False
        FieldName = 'feed_result'
        Font.Charset = ANSI_CHARSET
        Font.Color = clGreen
        Font.Height = -55
        Font.Name = #24494#36719#38597#40657
        Font.Style = [fsBold]
        Title.Alignment = taCenter
        Title.Caption = #19978#26009
        Title.Font.Charset = ANSI_CHARSET
        Title.Font.Color = clWindowText
        Title.Font.Height = -33
        Title.Font.Name = #24494#36719#38597#40657
        Title.Font.Style = []
        Width = 197
        Visible = True
      end
      item
        Alignment = taCenter
        Expanded = False
        FieldName = 'change_result'
        Font.Charset = ANSI_CHARSET
        Font.Color = clGreen
        Font.Height = -55
        Font.Name = #24494#36719#38597#40657
        Font.Style = [fsBold]
        Title.Alignment = taCenter
        Title.Caption = #25442#26009#13#10
        Title.Font.Charset = ANSI_CHARSET
        Title.Font.Color = clWindowText
        Title.Font.Height = -33
        Title.Font.Name = #24494#36719#38597#40657
        Title.Font.Style = []
        Width = 213
        Visible = True
      end
      item
        Alignment = taCenter
        Expanded = False
        FieldName = 'check_result'
        Font.Charset = ANSI_CHARSET
        Font.Color = clGreen
        Font.Height = -55
        Font.Name = #24494#36719#38597#40657
        Font.Style = [fsBold]
        Title.Alignment = taCenter
        Title.Caption = #26680#26009
        Title.Font.Charset = ANSI_CHARSET
        Title.Font.Color = clWindowText
        Title.Font.Height = -33
        Title.Font.Name = #24494#36719#38597#40657
        Title.Font.Style = []
        Width = 211
        Visible = True
      end
      item
        Alignment = taCenter
        Expanded = False
        FieldName = 'check_all_result'
        Font.Charset = ANSI_CHARSET
        Font.Color = clGreen
        Font.Height = -55
        Font.Name = #24494#36719#38597#40657
        Font.Style = [fsBold]
        Title.Alignment = taCenter
        Title.Caption = #20840#26816
        Title.Font.Charset = ANSI_CHARSET
        Title.Font.Color = clWindowText
        Title.Font.Height = -33
        Title.Font.Name = #24494#36719#38597#40657
        Title.Font.Style = []
        Width = 223
        Visible = True
      end
      item
        Alignment = taCenter
        Expanded = False
        FieldName = 'first_check_all_result'
        Font.Charset = ANSI_CHARSET
        Font.Color = clGreen
        Font.Height = -55
        Font.Name = #24494#36719#38597#40657
        Font.Style = [fsBold]
        Title.Alignment = taCenter
        Title.Caption = #39318#26816
        Title.Font.Charset = ANSI_CHARSET
        Title.Font.Color = clWindowText
        Title.Font.Height = -33
        Title.Font.Name = #24494#36719#38597#40657
        Title.Font.Style = []
        Width = 240
        Visible = True
      end>
  end
  object Panel1: TPanel
    Left = 0
    Top = 0
    Width = 1440
    Height = 283
    Align = alClient
    TabOrder = 1
    object Label6: TLabel
      Left = 24
      Top = 96
      Width = 70
      Height = 46
      Caption = #32447#21495
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label2: TLabel
      Left = 20
      Top = 179
      Width = 140
      Height = 46
      Caption = #21407#22987#31449#20301
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label3: TLabel
      Left = 20
      Top = 255
      Width = 140
      Height = 46
      Caption = #25195#25551#31449#20301
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label4: TLabel
      Left = 490
      Top = 181
      Width = 140
      Height = 46
      Caption = #21407#22987#26009#21495
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label5: TLabel
      Left = 490
      Top = 250
      Width = 140
      Height = 46
      Caption = #25195#25551#26009#21495
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label7: TLabel
      Left = 305
      Top = 96
      Width = 70
      Height = 46
      Caption = #24037#21333
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label8: TLabel
      Left = 710
      Top = 97
      Width = 140
      Height = 46
      Caption = #26495#38754#31867#22411
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object Label1: TLabel
      Left = 16
      Top = 26
      Width = 105
      Height = 46
      Alignment = taCenter
      Caption = #25805#20316#21592
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -35
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentFont = False
    end
    object scanLineseatLb: TLabel
      Left = 167
      Top = 250
      Width = 305
      Height = 52
      AutoSize = False
      Color = clSilver
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -40
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object lineseatLb: TLabel
      Left = 167
      Top = 177
      Width = 306
      Height = 52
      AutoSize = False
      Color = clSilver
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -40
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object materialNoLb: TLabel
      Left = 640
      Top = 177
      Width = 369
      Height = 52
      AutoSize = False
      Color = clSilver
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -40
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object scanMaterialNoLb: TLabel
      Left = 638
      Top = 252
      Width = 367
      Height = 52
      AutoSize = False
      Color = clSilver
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -40
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object operatorLb: TLabel
      Left = 149
      Top = 28
      Width = 186
      Height = 44
      AutoSize = False
      Color = clScrollBar
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -33
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
    end
    object typeLb: TLabel
      Left = 1021
      Top = 18
      Width = 420
      Height = 159
      Alignment = taCenter
      AutoSize = False
      Caption = #25805#20316
      Color = clGreen
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindow
      Font.Height = -117
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
      WordWrap = True
    end
    object resultLb: TLabel
      Left = 1021
      Top = 162
      Width = 420
      Height = 155
      Alignment = taCenter
      AutoSize = False
      Caption = #32467#26524
      Color = clGreen
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindow
      Font.Height = -117
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ParentColor = False
      ParentFont = False
      WordWrap = True
    end
    object lineCb: TComboBox
      Left = 102
      Top = 97
      Width = 190
      Height = 44
      Style = csDropDownList
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -27
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ItemHeight = 36
      ParentFont = False
      TabOrder = 0
      OnChange = lineCbChange
      Items.Strings = (
        '301'
        '302'
        '303'
        '304'
        '305'
        '306'
        '307'
        '308')
    end
    object workOrderCb: TComboBox
      Left = 381
      Top = 96
      Width = 320
      Height = 44
      Style = csDropDownList
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -27
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ItemHeight = 36
      ParentFont = False
      TabOrder = 1
      OnChange = workOrderCbChange
    end
    object boardTypeCb: TComboBox
      Left = 858
      Top = 98
      Width = 151
      Height = 44
      Style = csDropDownList
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -27
      Font.Name = #24494#36719#38597#40657
      Font.Style = [fsBold]
      ItemHeight = 36
      ParentFont = False
      TabOrder = 2
      OnChange = boardTypeCbChange
    end
  end
  object DBConnection: TADOConnection
    Connected = True
    ConnectionString = 'Provider=MSDASQL.1;Persist Security Info=False;Data Source=mysql'
    LoginPrompt = False
    Left = 369
    Top = 32
  end
  object selectQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 432
    Top = 32
  end
  object mainQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 480
    Top = 32
  end
  object operatorQry: TADOQuery
    Connection = DBConnection
    Parameters = <>
    Prepared = True
    Left = 536
    Top = 32
  end
  object ds: TDataSource
    DataSet = mainQry
    Left = 600
    Top = 32
  end
  object refreshTimer: TTimer
    Interval = 5000
    OnTimer = refreshTimerTimer
    Left = 664
    Top = 32
  end
end
